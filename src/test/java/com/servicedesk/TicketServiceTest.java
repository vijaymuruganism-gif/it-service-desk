package com.servicedesk;

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
import com.servicedesk.service.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test suite for {@link TicketServiceImpl} using JUnit 5 and Mockito.
 */
@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private User employee;
    private User engineer;
    private Category category;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        employee = new User("alice", "alice@test.com", "pass", Role.EMPLOYEE, "Finance");
        employee.setId(1L);

        engineer = new User("john_eng", "john@test.com", "pass", Role.SUPPORT_ENGINEER, "IT Support");
        engineer.setId(2L);

        category = new Category("Hardware", "Hardware peripherals");
        category.setId(10L);

        ticket = new Ticket("Broken Monitor", "Monitor does not power on", Priority.HIGH, category, employee);
        ticket.setId(100L);
    }

    @Test
    @DisplayName("createTicket() should save and return a ticket with OPEN status")
    void testCreateTicket_Success() {
        TicketRequestDto request = new TicketRequestDto("Broken Monitor", "Monitor does not power on", Priority.HIGH, 10L, 1L);

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(employee));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        TicketResponseDto response = ticketService.createTicket(request, "alice");

        assertNotNull(response);
        assertEquals("Broken Monitor", response.getTitle());
        assertEquals(TicketStatus.OPEN, response.getStatus());
        assertEquals("Hardware", response.getCategoryName());
        assertEquals("alice", response.getCreatedByUsername());

        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    @DisplayName("assignTicket() should successfully assign ticket to engineer and transition status to ASSIGNED")
    void testAssignTicket_Success() {
        when(ticketRepository.findById(100L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(2L)).thenReturn(Optional.of(engineer));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        TicketResponseDto response = ticketService.assignTicket(100L, 2L);

        assertNotNull(response);
        assertEquals(TicketStatus.ASSIGNED, response.getStatus());
        assertEquals("john_eng", response.getAssignedToUsername());
    }

    @Test
    @DisplayName("assignTicket() should throw BadRequestException when target user is an EMPLOYEE")
    void testAssignTicket_ThrowsBadRequest_WhenUserNotEngineer() {
        User anotherEmployee = new User("bob", "bob@test.com", "pass", Role.EMPLOYEE, "HR");
        anotherEmployee.setId(3L);

        when(ticketRepository.findById(100L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(3L)).thenReturn(Optional.of(anotherEmployee));

        assertThrows(BadRequestException.class, () -> ticketService.assignTicket(100L, 3L));
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    @DisplayName("getTicketById() should throw ResourceNotFoundException when ticket does not exist")
    void testGetTicketById_NotFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.getTicketById(999L));
    }

    @Test
    @DisplayName("addComment() should save and return resolution comment")
    void testAddComment_Success() {
        CommentRequestDto commentRequest = new CommentRequestDto("Tested HDMI port, working now.");
        Comment comment = new Comment("Tested HDMI port, working now.", ticket, engineer);
        comment.setId(500L);

        when(ticketRepository.findById(100L)).thenReturn(Optional.of(ticket));
        when(userRepository.findByUsername("john_eng")).thenReturn(Optional.of(engineer));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDto response = ticketService.addComment(100L, commentRequest, "john_eng");

        assertNotNull(response);
        assertEquals("Tested HDMI port, working now.", response.getContent());
        assertEquals("john_eng", response.getUsername());
        assertEquals(Role.SUPPORT_ENGINEER, response.getUserRole());
    }
}
