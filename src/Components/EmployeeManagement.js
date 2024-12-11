/** @jsxImportSource theme-ui */
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Container, Row, Col, Button, Form, Table, Card, Navbar, Nav, NavDropdown, Modal } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import './EmployeeManagement.css'; // Import custom CSS for attendance colors
import { faPlus, faMoon, faSun, faEdit, faTrash, faCalendarAlt, faSearch } from '@fortawesome/free-solid-svg-icons'; // Import icons
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const EmployeeManagement = () => {
    const [employees, setEmployees] = useState([]);
    const [searchName, setSearchName] = useState('');
    const [employeeData, setEmployeeData] = useState({
        name: '',
        age: '',
        contactNumber: '',
        address: '',
        photo: '',
        dateOfJoining: '',
        salaryPerDay: '',
        salaryPerMonth: '',
    });
    const [editingId, setEditingId] = useState(null);
    const [showCalendar, setShowCalendar] = useState(false);
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [attendanceHours, setAttendanceHours] = useState(0);
    const [attendanceEmployeeId, setAttendanceEmployeeId] = useState('');
    const [attendanceData, setAttendanceData] = useState([]);
    const [isDarkMode, setIsDarkMode] = useState(false);
    const [showAddEmployee, setShowAddEmployee] = useState(false); // State to toggle Add Employee form
    const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1); // Default to current month
    const navigate = useNavigate();

    useEffect(() => {
        fetchEmployees();
    }, []);

    useEffect(() => {
        // Fetch salary for all employees whenever the selected month changes
        const fetchSalaries = async () => {
            for (const employee of employees) {
                const salary = await fetchSalary(employee.id, selectedMonth);
                employee.salaryPerMonth = salary;
            }
            setEmployees([...employees]); // Update state to trigger re-render
        };

        if (employees.length > 0) {
            fetchSalaries();
        }
    }, [selectedMonth, employees]);

    const fetchEmployees = async () => {
        try {
            const response = await axios.get(`http://localhost:8081/employees`);
            if (response.status === 200) {
                const employeesData = response.data;
                setEmployees(employeesData);
            } else {
                throw new Error("Failed to fetch employees");
            }
        } catch (error) {
            console.error("Error fetching employees:", error);
            toast.error(`Error fetching employees: ${error.response?.data?.message || error.message}`);
        }
    };

    const fetchSalary = async (id, month) => {
        try {
            const response = await axios.get(`http://localhost:8081/employees/${id}/salary/${month}`);
            return response.data;
        } catch (error) {
            console.error("Error fetching salary:", error);
            toast.error(`Error fetching salary: ${error.response?.data?.message || error.message}`);
            return 0;
        }
    };

    const handleSearch = async () => {
        try {
            const response = await axios.get(`http://localhost:8081/employees/search?name=${searchName}`);
            setEmployees(response.data);
        } catch (error) {
            console.error("Error searching employees:", error);
            toast.error(`Error searching employees: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEmployeeData({ ...employeeData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingId) {
                await axios.put(`http://localhost:8081/employees/${editingId}`, employeeData);
            } else {
                await axios.post('http://localhost:8081/employees', employeeData);
            }
            setEmployeeData({
                name: '',
                age: '',
                contactNumber: '',
                address: '',
                photo: '',
                dateOfJoining: '',
                salaryPerDay: '',
                salaryPerMonth: '',
            });
            setEditingId(null);
            fetchEmployees();
            toast.success('Employee saved successfully!');
        } catch (error) {
            console.error("Error saving employee:", error);
            toast.error(`Error saving employee: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleEdit = (employee) => {
        setEmployeeData(employee);
        setEditingId(employee.id);
    };

    const handleDelete = async (id) => {
        try {
            await axios.delete(`http://localhost:8081/employees/${id}`);
            fetchEmployees();
            toast.success('Employee deleted successfully!');
        } catch (error) {
            console.error("Error deleting employee:", error);
            toast.error(`Error deleting employee: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleOpenCalendar = async (id) => {
        setAttendanceEmployeeId(id);
        setShowCalendar(true);
        await fetchAttendance(id);
    };

    const handleCloseCalendar = () => {
        setShowCalendar(false);
        setAttendanceHours(0);
    };

    const handleDateChange = (date) => {
        setSelectedDate(date);
    };

    const fetchAttendance = async (id) => {
        const month = selectedDate.getMonth() + 1; // Get the current month (1-12)
        try {
            const response = await axios.get(`http://localhost:8081/employees/${id}/attendance/${month}`);
            setAttendanceData(response.data);
        } catch (error) {
            console.error("Error fetching attendance:", error);
            toast.error(`Error fetching attendance: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleAttendanceSubmit = async () => {
        const day = selectedDate.getDate();
        const month = selectedDate.getMonth() + 1; // Get the current month (1-12)
        try {
            await axios.put(`http://localhost:8081/employees/${attendanceEmployeeId}/attendance/${month}/${day}?hours=${attendanceHours}`);
            await fetchAttendance(attendanceEmployeeId); // Fetch updated attendance after submission
            fetchEmployees(); // Refresh employee list to update salary
            handleCloseCalendar();
            toast.success('Attendance updated successfully!');
        } catch (error) {
            console.error("Error updating attendance:", error);
            toast.error(`Error updating attendance: ${error.response?.data?.message || error.message}`);
        }
    };

    const renderDayContent = (date) => {
        const day = date.getDate();
        const attendanceHours = attendanceData[day - 1];

        let className = 'attendance-day';
        let displayHours = '';

        if (attendanceHours === undefined) {
            className += ' no-attendance';
        } else if (attendanceHours >= 9) {
            className += ' full-attendance';
            displayHours = attendanceHours;
        } else if (attendanceHours >= 4.5) {
            className += ' half-attendance';
            displayHours = attendanceHours;
        } else {
            className += ' low-attendance';
            displayHours = attendanceHours;
        }

        return (
            <div className={className}>
                {displayHours}
            </div>
        );
    };

    // Function to toggle dark mode
    const toggleDarkMode = () => {
        setIsDarkMode(!isDarkMode);
    };

    // Month options for the dropdown
    const monthOptions = Array.from({ length: 12 }, (_, i) => ({
        value: i + 1,
        label: new Date(0, i).toLocaleString('default', { month: 'long' }),
    }));

    return (
        <div style={{ backgroundColor: isDarkMode ? '#343a40' : '#f8f9fa', minHeight: '100vh', color: isDarkMode ? '#ffffff' : '#000000' }}>
            <Navbar style={{ backgroundColor: isDarkMode ? '#495057' : '#ffffff' }} expand="lg" className="navbar-light">
                <Navbar.Brand href="#home">Employee Dashboard</Navbar.Brand>
                <Nav className="ms-auto">
                    <NavDropdown title="Profile" id="basic-nav-dropdown" align={'end'}>
                        <NavDropdown.Item onClick={() => navigate('/update-profile')}>Update Profile</NavDropdown.Item>
                        <NavDropdown.Item onClick={() => navigate('/menu')}>Update Menu</NavDropdown.Item>
                        <NavDropdown.Item onClick={() => { window.location.href = 'http://localhost:3000'; }}>Logout</NavDropdown.Item>
                    </NavDropdown>
                    <Button onClick={toggleDarkMode} variant={isDarkMode ? "light" : "dark"} className="ms-2" style={{ fontSize: '0.8rem', marginTop: '2px'}}>
                        <FontAwesomeIcon icon={isDarkMode ? faSun : faMoon} />
                    </Button>
                </Nav>
            </Navbar>
            <Container>
                <Row>
                    <Col md={4} className="mb-4" style={{ paddingTop: '2px' }}>
                        <Button
                            variant="success"
                            onClick={() => setShowAddEmployee(!showAddEmployee)}
                            style={{ marginBottom: '10px', borderRadius: '50%', width: '40px', height: '40px', display: 'left', alignItems: 'center', justifyContent: 'left' }}
                        >
                            <FontAwesomeIcon icon={faPlus} />
                        </Button>
                        {showAddEmployee && (
                            <Card>
                                <Card.Body>
                                    <h2>{editingId ? 'Edit Employee' : 'Add Employee'}</h2>
                                    <Form onSubmit={handleSubmit}>
                                        <Form.Group controlId="name">
                                            <Form.Label>First Name *</Form.Label>
                                            <Form.Control type="text" name="name" value={employeeData.name} onChange={handleChange} required />
                                        </Form.Group>
                                        <Form.Group controlId="age">
                                            <Form.Label>Age *</Form.Label>
                                            <Form.Control type="number" name="age" value={employeeData.age} onChange={handleChange} required />
                                        </Form.Group>
                                        <Form.Group controlId="contactNumber">
                                            <Form.Label>Contact Number *</Form.Label>
                                            <Form.Control type="text" name="contactNumber" value={employeeData.contactNumber} onChange={handleChange} required />
                                        </Form.Group>
                                        <Form.Group controlId="address">
                                            <Form.Label>Address *</Form.Label>
                                            <Form.Control type="text" name="address" value={employeeData.address} onChange={handleChange} required />
                                        </Form.Group>
                                        <Form.Group controlId="photo">
                                            <Form.Label>Photo URL</Form.Label>
                                            <Form.Control type="text" name="photo" value={employeeData.photo} onChange={handleChange} />
                                        </Form.Group>
                                        <Form.Group controlId="dateOfJoining">
                                            <Form.Label>Date of Joining *</Form.Label>
                                            <Form.Control type="date" name="dateOfJoining" value={employeeData.dateOfJoining} onChange={handleChange} required />
                                        </Form.Group>
                                        <Form.Group controlId="salaryPerDay">
                                            <Form.Label>Salary Per Day *</Form.Label>
                                            <Form.Control type="number" name="salaryPerDay" value={employeeData.salaryPerDay} onChange={handleChange} required />
                                        </Form.Group>
                                        <Form.Group controlId="salaryPerMonth">
                                            <Form.Label>Salary Per Month</Form.Label>
                                            <Form.Control type="number" name="salaryPerMonth" value={employeeData.salaryPerMonth} onChange={handleChange} disabled />
                                        </Form.Group>
                                        <Button type="submit" variant="primary" className="mt-3">{editingId ? 'Update' : 'Add'}</Button>
                                    </Form>
                                </Card.Body>
                            </Card>
                        )}
                    </Col>
                    <Col md={22} style={{ paddingTop: '2px', display: 'flex', justifyContent: 'center'}}>
                        <Card className="mb-4" style={{ width: '100%' }}>
                            <Card.Body>
                                <h2>Employee</h2>
                                <Form.Group controlId="search" style={{ marginBottom: '10px' }}>
                                    <Form.Label>Search by Name</Form.Label>
                                    <div style={{ display: 'flex', alignItems: 'center' }}>
                                        <Form.Control
                                            type="text"
                                            value={searchName}
                                            onChange={(e) => setSearchName(e.target.value)}
                                            placeholder="Enter employee name"
                                            style={{ marginRight: '40px' }}
                                        />
                                        <Button onClick={handleSearch} variant="primary" style={{ borderRadius: '50%',marginBottom: '20px', borderRadius: '50%', width: '40px', height: '40px', display: 'left', alignItems: 'center', justifyContent: 'left', marginRight: '300px' }}>
                                            <FontAwesomeIcon icon={faSearch} />
                                        </Button>
                                    </div>
                                </Form.Group>
                                <Form.Group controlId="salaryMonth" style={{ marginBottom: '10px' }}>
                                    <Form.Label>Select Month for Salary</Form.Label>
                                    <Form.Control as="select" value={selectedMonth} onChange={(e) => setSelectedMonth(e.target.value)}>
                                        {monthOptions.map(month => (
                                            <option key={month.value} value={month.value}>{month.label}</option>
                                        ))}
                                    </Form.Control>
                                </Form.Group>
                                <Table striped bordered hover className="mt-3">
                                    <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th> Age </th>
                                            <th>Contact Number</th>
                                            <th>Address</th>
                                            <th>Photo</th>
                                            <th>Date of Joining</th>
                                            <th>Salary Per Day</th>
                                            <th>Salary Per Month</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {employees.map(employee => (
                                            <tr key={employee.id}>
                                                <td>{employee.name}</td>
                                                <td>{employee.age}</td>
                                                <td>{employee.contactNumber}</td>
                                                <td>{employee.address}</td>
                                                <td><img src={employee.photo} alt={employee.name} style={{ width: '50px' }} /></td>
                                                <td>{new Date(employee.dateOfJoining).toLocaleDateString()}</td>
                                                <td>{employee.salaryPerDay}</td>
                                                <td>{employee.salaryPerMonth}</td>
                                                <td>
                                                    <Button variant="info" onClick={() => handleEdit(employee)}>
                                                        <FontAwesomeIcon icon={faEdit} />
                                                    </Button>
                                                    <Button variant="danger" onClick={() => handleDelete(employee.id)}>
                                                        <FontAwesomeIcon icon={faTrash} />
                                                    </Button>
                                                    <Button variant="warning" onClick={() => handleOpenCalendar(employee.id)}>
                                                        <FontAwesomeIcon icon={faCalendarAlt} />
                                                    </Button>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </Table>
                            </Card.Body>
                        </Card>
                        {/* Modal for Calendar */}
                        <Modal show={showCalendar} onHide={handleCloseCalendar}>
                            <Modal.Header closeButton>
                                <Modal.Title>Update Attendance</Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                <Calendar
                                    onChange={handleDateChange}
                                    value={selectedDate}
                                    tileContent={({ date }) => renderDayContent(date)}
                                />
                                <Form.Group controlId="attendanceHours">
                                    <Form.Label>Working Hours</Form.Label>
                                    <Form.Control
                                        type="number"
                                        value={attendanceHours}
                                        onChange={(e) => setAttendanceHours(e.target.value)}
                                        placeholder="Enter working hours"
                                    />
                                </Form.Group>
                            </Modal.Body>
                            <Modal.Footer>
                                <Button variant="secondary" onClick={handleCloseCalendar}>
                                    Close
                                </Button>
                                <Button variant="primary" onClick={handleAttendanceSubmit}>
                                    Save Changes
                                </Button>
                            </Modal.Footer>
                        </Modal>
                    </Col>
                </Row>
            </Container>
        </div>
    );
};

export default EmployeeManagement;