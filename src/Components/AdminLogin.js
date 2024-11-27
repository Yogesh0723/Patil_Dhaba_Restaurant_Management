import React, { useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom'; // Import useNavigate

const AdminLogin = () => {
    const [credentials, setCredentials] = useState({
        username: '',
        password: ''
    });
    const [error, setError] = useState(null);
    const navigate = useNavigate(); // Initialize useNavigate

    const handleChange = (e) => {
        setCredentials({ ...credentials, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8081/admin/login', credentials); // Use relative URL for proxy
            toast.success(response.data.message);
            // Navigate to the dashboard after successful login
            navigate('/dashboard'); // Change '/dashboard' to your actual dashboard route
            console.log(response.data);
            alert('User Login successful');
        } catch (error) {
            setError(error.response?.data?.message || 'Error logging in');
            toast.error(error.response?.data?.message || 'Error logging in');
        }
    };

    return (
        <div className="container">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-body">
                            <h2>Admin Login</h2>
                            <form onSubmit={handleSubmit}>
                                <div className="form-group">
                                    <label htmlFor="username">Username:</label>
                                    <input
                                        type="text"
                                        id="username"
                                        name="username"
                                        value={credentials.username}
                                        onChange={handleChange}
                                        className="form-control"
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="password">Password:</label>
                                    <input
                                        type="password"
                                        id="password"
                                        name="password"
                                        value={credentials.password}
                                        onChange={handleChange}
                                        className="form-control"
                                        required
                                    />
                                </div>
                                <button type="submit" className="btn btn-primary">
                                    Login
                                </button>
                            </form>
                            {error && (
                                <div className="alert alert-danger mt-3">{error}</div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdminLogin;