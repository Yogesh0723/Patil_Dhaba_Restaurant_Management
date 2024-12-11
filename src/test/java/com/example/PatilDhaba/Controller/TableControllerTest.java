package com.example.PatilDhaba.Controller;

import com.example.PatilDhaba.model.OrderItem;
import com.example.PatilDhaba.model.Table;
import com.example.PatilDhaba.service.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TableControllerTest {

    @InjectMocks
    private TableController tableController;

    @Mock
    private TableService tableService;

    private Table sampleTable;
    private OrderItem sampleOrderItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleTable = new Table();
        sampleTable.setTableNumber(1);
        sampleTable.setTotalAmount(100.0);

        sampleOrderItem = new OrderItem();
        sampleOrderItem.setMenuItemName("Naan");
        sampleOrderItem.setPrice(50.0);
        sampleOrderItem.setQuantity(2);
    }

    @Test
    void createTable_success() {
        when(tableService.createTable(1)).thenReturn(sampleTable);

        ResponseEntity<Table> response = tableController.createTable(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleTable, response.getBody());
        verify(tableService, times(1)).createTable(1);
    }

    @Test
    void createTable_failure() {
        when(tableService.createTable(1)).thenThrow(new IllegalArgumentException("Table already exists"));

        ResponseEntity<Table> response = tableController.createTable(1);

        assertEquals(400, response.getStatusCodeValue());
        verify(tableService, times(1)).createTable(1);
    }

    @Test
    void addOrderItem_success() {
        when(tableService.addOrderItem(1, sampleOrderItem)).thenReturn(sampleTable);

        ResponseEntity<Table> response = tableController.addOrderItem(1, sampleOrderItem);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleTable, response.getBody());
        verify(tableService, times(1)).addOrderItem(1, sampleOrderItem);
    }

    @Test
    void applyDiscount_success() {
        when(tableService.applyDiscount(1, 10.0)).thenReturn(sampleTable);

        ResponseEntity<Table> response = tableController.applyDiscount(1, 10.0);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleTable, response.getBody());
        verify(tableService, times(1)).applyDiscount(1, 10.0);
    }

    @Test
    void clearTable_success() {
        doNothing().when(tableService).clearTable(1);

        ResponseEntity<Void> response = tableController.clearTable(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(tableService, times(1)).clearTable(1);
    }

    @Test
    void getTable_success() {
        when(tableService.getTable(1)).thenReturn(sampleTable);

        ResponseEntity<Table> response = tableController.getTable(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleTable, response.getBody());
        verify(tableService, times(1)).getTable(1);
    }

    @Test
    void getTable_notFound() {
        when(tableService.getTable(1)).thenReturn(null);

        ResponseEntity<Table> response = tableController.getTable(1);

        assertEquals(404, response.getStatusCodeValue());
        verify(tableService, times(1)).getTable(1);
    }

    @Test
    void getTodayProfit_success() {
        when(tableService.getTodayProfit()).thenReturn(500.0);

        ResponseEntity<Double> response = tableController.getTodayProfit();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(500.0, response.getBody());
        verify(tableService, times(1)).getTodayProfit();
    }

    @Test
    void settleBill_success() {
        doNothing().when(tableService).settleBill(1);

        ResponseEntity<Void> response = tableController.settleBill(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(tableService, times(1)).settleBill(1);
    }

    @Test
    void settleBill_failure() {
        doThrow(new IllegalArgumentException("Table not found"))
                .when(tableService).settleBill(1);

        ResponseEntity<Void> response = tableController.settleBill(1);

        assertEquals(400, response.getStatusCodeValue());
        verify(tableService, times(1)).settleBill(1);
    }
}