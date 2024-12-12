import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faInstagram, faFacebook, faLinkedin, faYoutube } from '@fortawesome/free-brands-svg-icons';
import { faDirections, faMapMarkerAlt   } from '@fortawesome/free-solid-svg-icons';// Import Font Awesome icons
import unsplashImage from '../Images/unsplash.jpg';
import LogoImage from '../Images/logo_no.PNG';
import aboutImage from '../Images/about.jpg';
import Dish1 from '../Images/Dish1.jpg';
import Dish2 from '../Images/Dish2.jpg';
import Dish3 from '../Images/Dish3.jpg';
import Dish4 from '../Images/Dish4.jpg';
import Dish5 from '../Images/Dish5.jpg';
import Dish6 from '../Images/Dish6.jpg';
import Dish7 from '../Images/Dish7.jpg';
import Dish8 from '../Images/Dish8.jpg';
import Dish9 from '../Images/Dish9.jpg';
import photo from '../Images/patilDhabaMain.jpg';

const dishes = [
    "Paneer Tikka",
    "Biryani",
    "Vada Pav",
    "Pav Bhaji",
    "Puran Poli",
    "Misal Pav",
    "Dhokla",
    "Samosa",
    "Bhaji",
    "Kothimbir Vadi"
];
const imageGallery = [
    { src: Dish1, alt: 'Dish 1' },
    { src: Dish2, alt: 'Dish 2' },
    { src: Dish3, alt: 'Dish 3' },
    { src: Dish4, alt: 'Dish 4' },
    { src: Dish5, alt: 'Dish 5' },
    { src: Dish6, alt: 'Dish 6' },
    { src: Dish7, alt: 'Dish 7' },
    { src: Dish8, alt: 'Dish 8' },
    { src: Dish9, alt: 'Dish 9' },
    { src: Dish6, alt: 'Dish 10' },
];

const Homepage = ({ handleRegisterClick, handleLoginClick }) => {
    const [currentDish, setCurrentDish] = useState(dishes[0]);
    const [currentImageIndex, setCurrentImageIndex] = useState(0);
    const [customerCount, setCustomerCount] = useState(10000);
    const [isHovered, setIsHovered] = useState(false);

    const handleMouseEnter = () => setIsHovered(true);
    const handleMouseLeave = () => setIsHovered(false);

    useEffect(() => {
        const dishInterval = setInterval(() => {
            setCurrentDish(dishes[(dishes.indexOf(currentDish) + 1) % dishes.length]);
        }, 1000);


        const imageInterval = setInterval(() => {
            setCurrentImageIndex((currentImageIndex + 1) % imageGallery.length);
        }, 1000);

        // Customer count increment every 2 minutes
        const countInterval = setInterval(() => {
            setCustomerCount(prevCount => prevCount + 1);
        }, 120000); // 120000 ms = 2 minutes

        return () => {
            clearInterval(dishInterval);
            clearInterval(imageInterval);
            clearInterval(countInterval);
        };
    }, [currentDish, currentImageIndex]);

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
                    <Link to="#about" className="nav-link">About</Link>
                    <a href="https://maps.app.goo.gl/1sFA4nPQHubDW7Xu9" target="_blank" rel="noopener noreferrer" className="nav-link">
                        <FontAwesomeIcon icon={faDirections} style={{ marginRight: '0px' }} />
                        Directions
                    </a>
                </div>
            </header>
            <div className={`main`}>
                <div className="left-side"style={{ display: 'flex',marginLeft: '500px',padding: '10px'}}><h2>{currentDish}</h2>
                </div>
                <div className="right-side" style={{ display: 'flex', marginTop: '20px' }}>
                    <div className="content">
                        <h1>A New Way to Savor Indian Cuisine</h1>
                        <p>Patil Dhaba brings you the best of Indian flavors, with a variety of dishes that will tantalize your taste buds and leave you craving for more.</p>
                        <img alt="Illustration of various Indian dishes and vegetables" width={150} style={{ marginLeft: '300px', display: 'flex' }} src={LogoImage} />
                    </div>
                    <div className="image" style={{ display: 'flex' }}>
                        <img alt="Illustration of various Indian dishes and vegetables" width={700} src={unsplashImage} style={{ borderRadius: '20px', borderTopRightRadius: '200px', borderBottomLeftRadius: '200px' }}/>
                    </div>
                </div>
            </div>
            <div className={`explore`}>
                <div className="content">
                    <h2>Start Exploring Our Menu</h2>
                    <p>Our menu is a well-curated selection of traditional and contemporary Indian dishes, crafted to provide you with an unforgettable dining experience. From spicy curries to delectable desserts, we have something for everyone.</p>
                </div>
                <div className="image-gallery">
                <div
                        className="image-item"
                        style={{
                            display: 'inline-block',
                            transition: 'box-shadow 0.3s ease-in-out',
                            boxShadow: isHovered
                                ? '0 0 15px 5px rgba(255, 105, 180, 0.6), 0 0 30px 15px rgba(255, 182, 193, 0.4)'
                                : 'none'
                        }}
                        onMouseEnter={handleMouseEnter}
                        onMouseLeave={handleMouseLeave}
                    >
                        <img
                            src={imageGallery[currentImageIndex].src}
                            alt={imageGallery[currentImageIndex].alt}
                        />
                    </div>
                </div>
            </div>
            <div id="about" className={`about`}>
                <h2 style={{marginLeft: '20px'}}>About Us</h2>
                <div className="about-content" style={{ display: 'flex', alignItems: 'center', backgroundColor: '#f0f0f0', padding: '20px', borderRadius: '8px' }}>
                    <img src={photo} alt="About Us" width="800" height="500" />
                    <div className="about-text" style={{ marginLeft: '20px' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                            <tbody>
                                <tr>
                                    <td style={{ padding: '10px', verticalAlign: 'top' }}>
                                        <h3>Blog</h3>
                                        <p>Since 2022, we have been dedicated to serving the best Indian food, focusing on quality and authenticity. Our dishes are crafted with love and passion, ensuring every bite is a delightful experience.</p>
                                        <p>Total Happy Customers: <strong>{customerCount}</strong> and Counting!</p>
                                    </td>
                                    <td style={{ padding: '10px', verticalAlign: 'top' }}>
                                        <h3>Our mission:</h3>
                                        <p>To serve the best Maharashtrian food of all time to the people. We aim to create a welcoming environment where everyone can enjoy the rich flavors of Indian cuisine.</p>
                                    </td>
                                </tr>
                                <tr>
                                    <td colSpan="2" style={{ padding: '10px', verticalAlign: 'top' }}>
                                        <a href="https://maps.app.goo.gl/1sFA4nPQHubDW7Xu9" target="_blank" rel="noopener noreferrer">
                                            <button style={{ padding: '10px 20px', backgroundColor: '#ffcc00', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>
                                                Visit Now  <FontAwesomeIcon icon={faMapMarkerAlt} />
                                            </button>
                                        </a>
                                    </td>
                                </tr>
                                <tr>
                                    <td colSpan="2" style={{ padding: '10px', verticalAlign: 'top' }}>
                                        <div className="social-links">
                                            <h4>Follow Us:</h4>
                                            <a href="https://www.instagram.com" target="_blank" rel="noopener noreferrer">
                                                <FontAwesomeIcon icon={faInstagram} size="lg" style={{ marginRight: '10px', color: '#E1306C' }} />
                                            </a>
                                            <a href="https://www.facebook.com" target="_blank" rel="noopener noreferrer">
                                                <FontAwesomeIcon icon={faFacebook} size="lg" style={{ marginRight: '10px', color: '#3b5998' }} />
                                            </a>
                                            <a href="https://www.linkedin.com" target="_blank" rel="noopener noreferrer">
                                                <FontAwesomeIcon icon={faLinkedin} size="lg" style={{ marginRight: '10px', color: '#0077B5' }} />
                                            </a>
                                            <a href="https://www.youtube.com" target="_blank" rel="noopener noreferrer">
                                                <FontAwesomeIcon icon={faYoutube} size="lg" style={{ marginRight: '10px', color: '#FF0000' }} />
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <footer style={{ background: '#f0f0f0', color: 'grey', padding: '10px 20px', textAlign: 'center', }}>
                <p>------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</p>
               <p>By continuing past this page, you agree to our Terms of Service, Cookie Policy, Privacy Policy and Content Policies. All trademarks are properties of their respective owners. 2022-2024 © Patil Dhaba Ltd. All rights reserved.</p> 
            </footer>
        </div>
    );
};

export default Homepage;