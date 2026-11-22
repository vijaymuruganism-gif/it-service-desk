package com.servicedesk.service;

import com.servicedesk.dto.CommentRequestDto;
import com.servicedesk.dto.CommentResponseDto;
import com.servicedesk.dto.TicketRequestDto;
import com.servicedesk.dto.TicketResponseDto;
import com.servicedesk.entity.*;
import com.servicedesk.exception.BadRequestException;
import com.servicedesk.exception.ResourceNotFoundException;
import com.servicedesk.repository.CategoryRepository;
import com.servicedesk.repository.CommentRepository;
import com.servicedesk.repository.TicketRepository;
import com.servicedesk.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link TicketService} managing ticket workflows, role-based filters, and comments.
 */
@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             CategoryRepository categoryRepository,
                             UserRepository userRepository,
                             CommentRepository commentRepository) {
        this.ticketRepository = ticketRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public TicketResponseDto createTicket(TicketRequestDto request, String currentUsername) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.getCategoryId()));

        // Resolve creator: prioritize current authenticated user if createdById is null or match
        User createdBy;
        if (currentUsername != null) {
            createdBy = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found: " + currentUsername));
        } else {
            createdBy = userRepository.findById(request.getCreatedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getCreatedById()));
        }

        Ticket ticket = new Ticket(
                request.getTitle(),
                request.getDescription(),
                request.getPriority(),
                category,
                createdBy
        );
        ticket.setStatus(TicketStatus.OPEN);

        Ticket savedTicket = ticketRepository.save(ticket);
        return mapToResponseDto(savedTicket);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponseDto> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponseDto> getMyTickets(String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));

        // Role-Based Filtering
        if (currentUser.getRole() == Role.EMPLOYEE) {
            // Employees see only the tickets they created
            return ticketRepository.findByCreatedBy(currentUser).stream()
                    .map(this::mapToResponseDto)
                    .collect(Collectors.toList());
        } else if (currentUser.getRole() == Role.SUPPORT_ENGINEER) {
            // Engineers see tickets assigned to them
            return ticketRepository.findByAssignedTo(currentUser).stream()
                    .map(this::mapToResponseDto)
                    .collect(Collectors.toList());
        } else {
            // Admins see all tickets
            return getAllTickets();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponseDto getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + id));
        return mapToResponseDto(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponseDto> getTicketsByStatus(TicketStatus status) {
        return ticketRepository.findByStatus(status).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponseDto> getTicketsByPriority(Priority priority) {
        return ticketRepository.findByPriority(priority).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponseDto assignTicket(Long ticketId, Long engineerId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + ticketId));

        User engineer = userRepository.findById(engineerId)
                .orElseThrow(() -> new ResourceNotFoundException("Engineer not found with ID: " + engineerId));

        if (engineer.getRole() != Role.SUPPORT_ENGINEER && engineer.getRole() != Role.ADMIN) {
            throw new BadRequestException("Tickets can only be assigned to IT Support Engineers or Admins. User role is: " + engineer.getRole());
        }

        ticket.setAssignedTo(engineer);
        if (ticket.getStatus() == TicketStatus.OPEN) {
            ticket.setStatus(TicketStatus.ASSIGNED);
        }

        Ticket updatedTicket = ticketRepository.save(ticket);
        return mapToResponseDto(updatedTicket);
    }

    @Override
    public TicketResponseDto updateTicketStatus(Long ticketId, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + ticketId));

        ticket.setStatus(newStatus);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return mapToResponseDto(updatedTicket);
    }

    @Override
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found with ID: " + id);
        }
        ticketRepository.deleteById(id);
    }

    @Override
    public CommentResponseDto addComment(Long ticketId, CommentRequestDto request, String currentUsername) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + ticketId));

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));

        Comment comment = new Comment(request.getContent(), ticket, user);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(
                savedComment.getId(),
                savedComment.getContent(),
                ticket.getId(),
                user.getId(),
                user.getUsername(),
                user.getRole(),
                savedComment.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsForTicket(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new ResourceNotFoundException("Ticket not found with ID: " + ticketId);
        }

        return commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId).stream()
                .map(c -> new CommentResponseDto(
                        c.getId(),
                        c.getContent(),
                        c.getTicket().getId(),
                        c.getUser().getId(),
                        c.getUser().getUsername(),
                        c.getUser().getRole(),
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    private TicketResponseDto mapToResponseDto(Ticket ticket) {
        TicketResponseDto dto = new TicketResponseDto();
        dto.setId(ticket.getId());
        dto.setTitle(ticket.getTitle());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setPriority(ticket.getPriority());

        if (ticket.getCategory() != null) {
            dto.setCategoryId(ticket.getCategory().getId());
            dto.setCategoryName(ticket.getCategory().getName());
        }

        if (ticket.getCreatedBy() != null) {
            dto.setCreatedById(ticket.getCreatedBy().getId());
            dto.setCreatedByUsername(ticket.getCreatedBy().getUsername());
            dto.setCreatedByEmail(ticket.getCreatedBy().getEmail());
        }

        if (ticket.getAssignedTo() != null) {
            dto.setAssignedToId(ticket.getAssignedTo().getId());
            dto.setAssignedToUsername(ticket.getAssignedTo().getUsername());
        }

        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setUpdatedAt(ticket.getUpdatedAt());

        return dto;
    }
}
