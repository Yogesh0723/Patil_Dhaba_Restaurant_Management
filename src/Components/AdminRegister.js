import React, { useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom'; // Import useNavigate for redirection

const AdminRegister = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        confirmPassword: '',
        email: '',
        firstName: '',
        lastName: '',
        phoneNumber: ''
    });
    const [message, setMessage] = useState({ type: '', content: '' });
    const navigate = useNavigate(); // Initialize useNavigate

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (formData.password !== formData.confirmPassword) {
            setMessage({ type: 'error', content: 'Passwords do not match' });
            toast.error('Passwords do not match');
            return;
        }
        try {
            const response = await axios.post('http://localhost:8081/admin/register', formData);
            setMessage({ type: 'success', content: response.data });
            toast.success(response.data);
            navigate('/login'); // Redirect to AdminLogin after successful registration
        } catch (error) {
            const errorMsg = error.response?.data?.message || 'Error registering admin';
            setMessage({ type: 'error', content: errorMsg });
            toast.error(errorMsg);
        }
    };

    return (
        <div className="container">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-body">
                            <h2>Admin Register</h2>
                            <form onSubmit={handleSubmit}>
                                <div className="form-group">
                                    <label htmlFor="username">Username:</label>
                                    <input type="text" name="username" placeholder="Username" onChange={handleChange} className="form-control" required />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="password">Password:</label>
                                    <input type="password" name="password" placeholder="Password" onChange={handleChange} className="form-control" required />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="confirmPassword">Confirm Password:</label>
                                    <input type="password" name="confirmPassword" placeholder="Confirm Password" onChange={handleChange} className="form-control" required />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="email">Email:</label>
                                    <input type="email" name="email" placeholder="Email" onChange={handleChange} className="form-control" required />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="firstName">First Name:</label>
                                    <input type="text" name="firstName" placeholder="First Name" onChange={handleChange} className="form-control" required />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="lastName">Last Name:</label>
                                    <input type="text" name="lastName" placeholder="Last Name" onChange={handleChange} className="form-control" required />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="phoneNumber">Phone Number:</label>
                                    <input type="text" name="phoneNumber" placeholder="Phone Number" onChange={handleChange} className="form-control" required />
                                </div>
                                {message.content && <div className={`message ${message.type}`}>{message.content}</div>}
                                <button type="submit" className="btn btn-primary">Register</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdminRegister;