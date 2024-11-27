import React from 'react';
import { Link } from 'react-router-dom';
import unsplashImage from '../Images/unsplash.jpg';
import LogoImage from '../Images/logo_no.PNG';

const Homepage = ({ handleRegisterClick, handleLoginClick }) => {
    return (
        <div className="App">
            <header className="header">
                <div className="logo">
                    <img alt="Patil Dhaba Logo" src={LogoImage} />
                    <span style={{ color: '#ffcc00', fontSize: '24px', fontWeight: 'bold' }}>
                        Patil Dhaba
                    </span>
                </div>
                <div className="nav">
                    <Link to="/register" className="nav-link" onClick={handleRegisterClick}>Register</Link>
                    <Link to="/login" className="nav-link" onClick={handleLoginClick}>Login</Link>
                </div>
            </header>
            <div className="main">
                <div className="content">
                    <h1>A New Way to Savor Indian Cuisine</h1>
                    <p>Patil Dhaba brings you the best of Indian flavors, with a variety of dishes that will tantalize your taste buds and leave you craving for more.</p>
                </div>
                <div className="image">
                    <img alt="Illustration of various Indian dishes and vegetables" src={unsplashImage} />
                </div>
            </div>
            <div className="explore">
                <div className="content">
                    <h2>Start Exploring Our Menu</h2>
                    <p>Our menu is a well-curated selection of traditional and contemporary Indian dishes, crafted to provide you with an unforgettable dining experience. From spicy curries to delectable desserts, we have something for everyone.</p>
                </div>
                <div className="image">
                    <img alt="Illustration of various Indian dishes and vegetables" src={unsplashImage} />
                </div>
            </div>
        </div>
    );
};

export default Homepage;