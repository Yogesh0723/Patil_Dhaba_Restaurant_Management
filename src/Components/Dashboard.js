/** @jsxImportSource theme-ui */
import React, { useEffect, useState, useCallback } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Container, Row, Col, Button, Form, Table, Badge, Card, ListGroup, Navbar, Nav, NavDropdown } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom'; // Import useNavigate
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'; // Import FontAwesome
import { faUserCircle, faSun, faMoon } from '@fortawesome/free-solid-svg-icons'; // Import user icon and theme icons

const Dashboard = () => {
    const initialTables = [];
    const [tables, setTables] = useState(initialTables);
    const [todayProfit, setTodayProfit] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [menuItems, setMenuItems] = useState([]);
    const [filteredMenuItems, setFilteredMenuItems] = useState([]);
    const [selectedTable, setSelectedTable] = useState(null);
    const [menuItemName, setMenuItemName] = useState('');
    const [quantity, setQuantity] = useState(1);
    const [discount, setDiscount] = useState(0);
    const [newTableNumber, setNewTableNumber] = useState('');
    const [isDarkMode, setIsDarkMode] = useState(false); // State for dark mode
    const navigate = useNavigate(); // Initialize useNavigate

    useEffect(() => {
        fetchTodayProfit();
        fetchMenuItems();
    }, []);

    const fetchMenuItems = async () => {
        try {
            const response = await axios.get('http://localhost:8081/menu/items');
            if (response.status === 200) {
                setMenuItems(response.data);
            } else {
                throw new Error("Failed to fetch menu items");
            }
        } catch (error) {
            console.error("Error fetching menu items:", error);
            toast.error(`Error fetching menu items: ${error.response?.data?.message || error.message}`);
            setError(error);
        }
    };

    const handleMenuItemSearch = (e) => {
        const value = e.target.value;
        setMenuItemName(value);
        if (value) {
            const filtered = menuItems.filter(item => item.name.toLowerCase().includes(value.toLowerCase()));
            setFilteredMenuItems(filtered);
        } else {
            setFilteredMenuItems([]);
        }
    };

    const handleMenuItemSelect = (item) => {
        setMenuItemName(item.name);
        setFilteredMenuItems([]);
    };

    const createTable = async (tableNumber) => {
        try {
            const response = await axios.post(`http://localhost:8081/tables/create?tableNumber=${tableNumber}`);
            if (response.status === 200) {
                await fetchTableData(tableNumber);
                toast.success(`Table ${tableNumber} created`);
            } else {
                throw new Error(`Failed to create table ${tableNumber}`);
            }
        } catch (error) {
            console.error(`Error creating table ${tableNumber}:`, error);
            toast.error(`Error creating table ${tableNumber}: ${error.response?.data?.message || error.message}`);
            setError(error);
        }
    };

    const fetchTableData = useCallback(async (tableNumber) => {
        try {
            const response = await axios.get(`http://localhost:8081/tables/${parseInt(tableNumber)}`);
            if (response.status === 200) {
                const updatedTables = [...tables];
                const index = updatedTables.findIndex(table => table.tableNumber === tableNumber);
                if (index !== -1) {
                    updatedTables[index] = response.data;
                } else {
                    updatedTables.push(response.data);
                }
                setTables(updatedTables);
            } else {
                if (response.status === 404) {
                    await createTable(tableNumber);
                } else {
                    throw new Error(`Failed to fetch table data for table ${tableNumber}`);
                }
            }
        } catch (error) {
            console.error(`Error fetching data for table ${tableNumber}:`, error);
            toast.error(`Error fetching data for table ${tableNumber}: ${error.response?.data?.message || error.message}`);
            setError(error);
        }
    }, [tables]);

    const fetchTodayProfit = useCallback(async () => {
        try {
            const response = await axios.get(`http://localhost:8081/tables/profit/today`);
            if (response.status === 200) {
                setTodayProfit(response.data);
            } else {
                throw new Error("Failed to fetch today's profit");
            }
        } catch (error) {
            console.error("Error fetching today's profit:", error);
            toast.error(`Error fetching today's profit: ${error.response?.data?.message || error.message}`);
            setError(error);
        }
    }, []);

    const addOrderItem = async (tableNumber, orderItem) => {
        try {
            const response = await axios.post(`http://localhost:8081/tables/orderItem/add/${tableNumber}`, orderItem);
            if (response.status === 200) {
                await fetchTableData(tableNumber);
                toast.success(`Added ${orderItem.quantity} x ${orderItem.menuItemName} to table ${tableNumber}`);
                fetchTodayProfit();
            } else {
                throw new Error(`Failed to add item to table ${tableNumber}`);
            }
        } catch (error) {
            console.error(`Error adding item to table ${tableNumber}:`, error);
            toast.error(`Error adding item to table ${tableNumber}: ${error.response?.data?.message || error.message}`);
            setError(error);
        }
    };

    const applyDiscount = async (tableNumber, discount) => {
        try {
            const response = await axios.put(`http://localhost:8081/tables/discount/${tableNumber}?discount=${discount}`);
            if (response.status === 200) {
                await fetchTableData(tableNumber);
                toast.success(`Discount applied to table ${tableNumber}`);
                fetchTodayProfit();
            } else {
                throw new Error(`Failed to apply discount to table ${tableNumber}`);
            }
        } catch (error) {
            console.error(`Error applying discount to table ${tableNumber}:`, error);
            toast.error(`Error applying discount to table ${tableNumber}: ${error.response?.data?.message || error.message}`);
            setError(error);
        }
    };

    const clearTable = async (tableNumber) => {
        try {
            const response = await axios.delete(`http://localhost:8081/tables/clear/${tableNumber}`);
            if (response.status === 204) {
                await fetchTableData(tableNumber);
                fetchTodayProfit();
                toast.success(`Cleared table ${tableNumber}`);
            } else {
                throw new Error(`Failed to clear table ${tableNumber}`);
            }
        } catch (error) {
            console.error(`Error clearing table ${tableNumber}:`, error);
            toast.error(`Error clearing table ${tableNumber}: ${error.response?.data?.message || error.message}`);
            setError(error);
        }
    };

    const handleAddItem = () => {
        addOrderItem(selectedTable, { menuItemName, quantity });
        setMenuItemName('');
        setQuantity(1);
    };

    const handleApplyDiscount = () => {
        applyDiscount(selectedTable, discount);
        setDiscount(0);
    };

    const handleClearTable = () => {
        clearTable(selectedTable);
    };

    const handleCreateTable = () => {
        createTable(newTableNumber);
        setNewTableNumber('');
    };

    const handleSettleBill = async () => {
        try {
            const response = await axios.post(`http://localhost:8081/tables/settle/${selectedTable}`);
            if (response.status === 204) {
                toast.success(`Bill settled for table ${selectedTable}`);
                fetchTodayProfit(); // Fetch today's profit after settling the bill
            } else {
                throw new Error(`Failed to settle bill for table ${selectedTable}`);
            }
        } catch (error) {
            console.error(`Error settling bill for table ${selectedTable}:`, error);
            toast.error(`Error settling bill for table ${selectedTable}: ${error.response?.data?.message || error.message}`);
            setError(error);
        }
    };

    const toggleTheme = () => {
        setIsDarkMode(!isDarkMode);
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error.message}</div>;
    }

    return (
        <div style={{ backgroundColor: isDarkMode ? '#121212' : '#ffffff', minHeight: '100vh' }}> {/* Background color based on theme */}
            <Navbar style={{ backgroundColor: isDarkMode ? '#ffffff' : '#343a40' }} expand="lg" className="navbar-dark"> {/* Navbar color based on theme */}
                <Navbar.Brand href="#home" style={{ color: isDarkMode ? '#000' : '#fff' }}>Restaurant Dashboard</Navbar.Brand>
                <Nav className="ms-auto">
                    <NavDropdown title={<FontAwesomeIcon icon={faUserCircle} style={{ color: isDarkMode ? '#000' : '#fff', fontSize: '1.5rem' }} />} id="basic-nav-dropdown" align={'end'}>
                        <NavDropdown.Item onClick={() => navigate('/update-profile')}>Update Profile</NavDropdown.Item> {/* Navigate to Update Profile */}
                        <NavDropdown.Item onClick={() => navigate('/menu')}>Update Menu</NavDropdown.Item> {/* Navigate to Menu */}
                        <NavDropdown.Item onClick={() => { window.location.href = 'http://localhost:3000'; }}>Logout</NavDropdown.Item> {/* Redirect to homepage */}
                    </NavDropdown>
                    <Nav.Link href="#settings" style={{ color: isDarkMode ? '#000' : '#fff' }}>Settings</Nav.Link> {/* Additional Nav Item */}
                    <Nav.Link href="#help" style={{ color: isDarkMode ? '#000' : '#fff' }}>Help</Nav.Link> {/* Additional Nav Item */}
                    <Button onClick={toggleTheme} variant={isDarkMode ? "outline-dark" : "outline-light"} style={{ marginLeft: '10px' }}>
                        <FontAwesomeIcon icon={isDarkMode ? faSun : faMoon} />
                    </Button> {/* Toggle button for light/dark mode */}
                </Nav>
            </Navbar>
            <Container>
                <Row>
                    <Col md={4} className="mb-4" style={{ paddingTop: '20px' }}> {/* Increased the size of the table column */}
                        <Card>
                            <Card.Body>
                                <h2>Tables</h2>
                                <ListGroup>
                                    {tables.map(table => (
                                        <ListGroup.Item
                                            key={table.tableNumber}
                                            onClick={() => setSelectedTable(table.tableNumber)}
                                            style={{ cursor: 'pointer' }}
                                        >
                                            Table {table.tableNumber}
                                        </ListGroup.Item>
                                    ))}
                                </ListGroup>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={8} style={{ paddingTop: '20px' }}> {/* Increased the size of the right column */}
                        <Card className="mb-4"> {/* Removed gradient background */}
                            <Card.Body>
                                <h2>Manage Table</h2>
                                <Form.Group controlId="newTableNumber">
                                    <Form.Label>Enter Table Number</Form.Label>
                                    <Form.Control
                                        type="number"
                                        placeholder="Table Number"
                                        value={newTableNumber}
                                        onChange={(e) => setNewTableNumber(parseInt(e.target.value))}
                                    />
                                    <Button className="mt-2" onClick={handleCreateTable} variant="primary">Create Table</Button>
                                </Form.Group>
                                <Form>
                                    <Row>
                                        <Col md={6}>
                                            <Form.Group controlId="menuItemName">
                                                <Form.Label>Menu Item Name</Form.Label>
                                                <Form.Control
                                                    type="text"
                                                    placeholder="Menu Item Name"
                                                    value={menuItemName}
                                                    onChange={handleMenuItemSearch}
                                                />
                                                {filteredMenuItems.length > 0 && (
                                                    <ListGroup>
                                                        {filteredMenuItems.map(item => (
                                                            <ListGroup.Item
                                                                key={item.id}
                                                                onClick={() => handleMenuItemSelect(item)}
                                                                style={{ cursor: 'pointer' }}
                                                            >
                                                                {item.name} - {item.price}
                                                            </ListGroup.Item>
                                                        ))}
                                                    </ListGroup>
                                                )}
                                            </Form.Group>
                                        </Col>
                                        <Col md={2}>
                                            <Form.Group controlId="quantity">
                                                <Form.Label>Quantity</Form.Label>
                                                <Form.Control
                                                    type="number"
                                                    placeholder="Quantity"
                                                    value={quantity}
                                                    onChange={(e) => setQuantity(parseInt(e.target.value))}
                                                />
                                            </Form.Group>
                                        </Col>
                                        <Col md={2} className="d-flex align-items-end">
                                            <Button onClick={handleAddItem} variant="primary">Add Item</Button>
                                        </Col>
                                    </Row>
                                </Form>
                                <Form>
                                    <Row className="align-items-end">
                                        <Col md={3}>
                                            <Form.Group controlId="discount">
                                                <Form.Label>Discount</Form.Label>
                                                <Form.Control
                                                    type="number"
                                                    placeholder="Discount"
                                                    value={discount}
                                                    onChange={(e) => setDiscount(parseFloat(e.target.value))}
                                                />
                                            </Form.Group>
                                        </Col>
                                        <Col md={2}>
                                            <Button onClick={handleApplyDiscount} variant="primary">Apply Discount</Button>
                                        </Col>
                                    </Row>
                                </Form>
                                <div className="d-flex justify-content-between mt-3"> {/* Added spacing between buttons */}
                                    <Button onClick={handleClearTable} variant="danger">Clear Table</Button>
                                    <Button onClick={handleSettleBill} variant="success">Settle Bill</Button>
                                </div>
                            </Card.Body>
                        </Card>

                        {selectedTable && (
                            <Card className="mb-4">
                                <Card.Body>
                                    <h2>Table {selectedTable}</h2>
                                    <Table striped bordered hover responsive>
                                        <thead>
                                            <tr>
                                                <th>Quantity</th>
                                                <th>Menu Item Name</th>
                                                <th>Price</th>
                                                <th>Order Time</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {tables.find(table => table.tableNumber === selectedTable).orderItems.map((item, index) => (
                                                <tr key={index}>
                                                    <td>{item.quantity}</td>
                                                    <td>{item.menuItemName}</td>
                                                    <td>${item.price}</td>
                                                    <td>{new Date(item.orderTime).toLocaleString()}</td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </Table>
                                    <p><Badge variant="secondary">Total Amount: ${tables.find(table => table.tableNumber === selectedTable).totalAmount}</Badge></p>
                                    <p><Badge variant="success">Final Amount: ${tables.find(table => table.tableNumber === selectedTable).totalAmount - tables.find(table => table.tableNumber === selectedTable).discount}</Badge></p>
                                </Card.Body>
                            </Card>
                        )}

                        <Card className="mb-4">
                            <Card.Body>
                                <h2>Today's Profit: ${todayProfit}</h2>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </div>
    );
};

export default Dashboard;