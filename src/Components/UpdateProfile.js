import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import { Container, Form, Button, Modal, Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const UpdateProfile = () => {
    const [adminDetails, setAdminDetails] = useState({
        username: '',
        email: '',
        password: '',
        newPassword: '',
        confirmPassword: '',
    });
    const [currentPassword, setCurrentPassword] = useState('');
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        // Fetch current admin details (you may need to adjust the endpoint)
        const fetchAdminDetails = async () => {
            try {
                const response = await axios.get('/admin/current'); 
                setAdminDetails({
                    ...adminDetails,
                    username: response.data.username,
                    email: response.data.email,
                });
            } catch (error) {
                console.log('Error fetching admin details');
            }
        };

        fetchAdminDetails();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setAdminDetails({ ...adminDetails, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (adminDetails.newPassword !== adminDetails.confirmPassword) {
            toast.error("New passwords do not match");
            return;
        }

        try {
            const response = await axios.put(`/admin/update/${adminDetails.username}?currentPassword=${currentPassword}`, {
                ...adminDetails,
            });
            toast.success(response.data);
            navigate('/dashboard'); // Redirect to dashboard after successful update
        } catch (error) {
            toast.error(error.response?.data || 'Error updating profile');
        }
    };

    const handleDeleteAccount = async () => {
        try {
            const response = await axios.delete(`/admin/delete/${adminDetails.username}`, {
                params: { currentPassword: currentPassword }, // Send current password for validation
            });
            toast.success(response.data);
            navigate('/'); // Redirect to homepage after successful deletion
        } catch (error) {
            toast.error(error.response?.data || 'Error deleting account');
        }
    };

    return (
        <Container>
            <h2>Update Profile</h2>
            <Form onSubmit={handleSubmit}>
                <Form.Group controlId="username">
                    <Form.Label>Username</Form.Label>
                    <Form.Control
                        type="text"
                        name="username"
                        value={adminDetails.username}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>
                <Form.Group controlId="email">
                    <Form.Label>Email</Form.Label>
                    <Form.Control
                        type="email"
                        name="email"
                        value={adminDetails.email}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>
                <Form.Group controlId="currentPassword">
                    <Form.Label>Current Password</Form.Label>
                    <Form.Control
                        type="password"
                        value={currentPassword}
                        onChange={(e) => setCurrentPassword(e.target.value)}
                        required
                    />
                </Form.Group>
                <Form.Group controlId="newPassword">
                    <Form.Label>New Password</Form.Label>
                    <Form.Control
                        type="password"
                        name="newPassword"
                        value={adminDetails.newPassword}
                        onChange={handleChange}
                    />
                </Form.Group>
                <Form.Group controlId="confirmPassword">
                    <Form.Label>Confirm New Password</Form.Label>
                    <Form.Control
                        type="password"
                        name="confirmPassword"
                        value={adminDetails.confirmPassword}
                        onChange={handleChange}
                    />
                </Form.Group>
                <Row className="mt-3">
                    <Col>
                        <Button variant="primary" type="submit" className="me-2">
                            Update Profile
                        </Button>
                    </Col>
                    <Col>
                        <Button variant="danger" onClick={() => setShowDeleteModal(true)}>
                            Delete Account
                        </Button>
                    </Col>
                </Row>
            </Form>

            {/* Confirmation Modal for Deleting Account */}
            <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Deletion</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>Are you sure you want to delete your account? This action cannot be undone.</p>
                    <Form.Group controlId="deleteCurrentPassword">
                        <Form.Label>Current Password</Form.Label>
                        <Form.Control
                            type="password"
                            value={currentPassword}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                            required
                        />
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleDeleteAccount}>
                        Delete Account
                    </Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
};

export default UpdateProfile;