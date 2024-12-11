package com.example.PatilDhaba.service;

import com.example.PatilDhaba.model.MenuItem;
import com.example.PatilDhaba.model.OrderItem;
import com.example.PatilDhaba.model.Table;
import com.example.PatilDhaba.repository.MenuItemRepository;
import com.example.PatilDhaba.repository.TableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TableServiceTest {

    @Mock
    private TableRepository tableRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTable_NewTable() {
        int tableNumber = 1;
        when(tableRepository.findByTableNumber(tableNumber)).thenReturn(Optional.empty());
        when(tableRepository.findAllKotNumbers()).thenReturn(Collections.emptyList());
        when(tableRepository.save(any(Table.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Table result = tableService.createTable(tableNumber);

        assertNotNull(result);
        assertEquals(tableNumber, result.getTableNumber());
        assertEquals(1, result.getKotNumber());
        verify(tableRepository, times(1)).save(result);
    }

    @Test
    void testCreateTable_ExistingTable() {
        int tableNumber = 1;
        Table existingTable = new Table();
        existingTable.setTableNumber(tableNumber);
        when(tableRepository.findByTableNumber(tableNumber)).thenReturn(Optional.of(existingTable));

        Table result = tableService.createTable(tableNumber);

        assertNotNull(result);
        assertEquals(tableNumber, result.getTableNumber());
        verify(tableRepository, never()).save(any(Table.class));
    }

    @Test
    void testAddOrderItem_Success() {
        int tableNumber = 1;
        String menuItemName = "Pizza";
        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemName);
        menuItem.setPrice(200.0);

        OrderItem orderItem = new OrderItem();
        orderItem.setMenuItemName(menuItemName);
        orderItem.setQuantity(2);

        Table table = new Table();
        table.setTableNumber(tableNumber);
        table.setTotalAmount(0);

        when(tableRepository.findByTableNumber(tableNumber)).thenReturn(Optional.of(table));
        when(menuItemRepository.findByNameIgnoreCase(menuItemName)).thenReturn(Optional.of(menuItem));
        when(tableRepository.save(any(Table.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Table result = tableService.addOrderItem(tableNumber, orderItem);

        assertNotNull(result);
        assertEquals(400.0, result.getTotalAmount());
        assertEquals(1, result.getOrderItems().size());
        verify(tableRepository, times(1)).save(result);
    }

    @Test
    void testAddOrderItem_MenuItemNotFound() {
        int tableNumber = 1;
        String menuItemName = "Pizza";

        OrderItem orderItem = new OrderItem();
        orderItem.setMenuItemName(menuItemName);

        when(menuItemRepository.findByNameIgnoreCase(menuItemName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.addOrderItem(tableNumber, orderItem));

        assertEquals("Menu item not found: " + menuItemName, exception.getMessage());
    }

    @Test
    void testApplyDiscount_Success() {
        int tableNumber = 1;
        double discount = 50.0;

        Table table = new Table();
        table.setTableNumber(tableNumber);

        when(tableRepository.findByTableNumber(tableNumber)).thenReturn(Optional.of(table));
        when(tableRepository.save(any(Table.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Table result = tableService.applyDiscount(tableNumber, discount);

        assertNotNull(result);
        assertEquals(discount, result.getDiscount());
        verify(tableRepository, times(1)).save(result);
    }

    @Test
    void testApplyDiscount_TableNotFound() {
        int tableNumber = 1;
        double discount = 50.0;

        when(tableRepository.findByTableNumber(tableNumber)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.applyDiscount(tableNumber, discount));

        assertEquals("Table not found", exception.getMessage());
    }

    @Test
    void testGetTodayProfit() {
        Table table1 = new Table();
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setPrice(100.0);
        orderItem1.setQuantity(2);
        orderItem1.setOrderTime(LocalDateTime.now());
        table1.setOrderItems(List.of(orderItem1));

        Table table2 = new Table();
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setPrice(200.0);
        orderItem2.setQuantity(1);
        orderItem2.setOrderTime(LocalDateTime.now().minusDays(1));
        table2.setOrderItems(List.of(orderItem2));

        when(tableRepository.findAll()).thenReturn(Arrays.asList(table1, table2));

        double profit = tableService.getTodayProfit();

        assertEquals(200.0, profit);
    }
}
