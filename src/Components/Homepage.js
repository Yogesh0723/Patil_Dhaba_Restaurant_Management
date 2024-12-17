import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faInstagram, faFacebook, faLinkedin, faYoutube } from '@fortawesome/free-brands-svg-icons';
import { faDirections, faMapMarkerAlt, faTimes } from '@fortawesome/free-solid-svg-icons';

// Import all existing images
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
    "Paneer Tikka", "Biryani", "Vada Pav", "Pav Bhaji", 
    "Puran Poli", "Misal Pav", "Dhokla", "Samosa", 
    "Bhaji", "Kothimbir Vadi"
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

const Homepage = () => {
    const [currentDish, setCurrentDish] = useState(dishes[0]);
    const [currentImageIndex, setCurrentImageIndex] = useState(0);
    const [customerCount, setCustomerCount] = useState(10000);
    const [isHovered, setIsHovered] = useState(false);
    const navigate = useNavigate();
    
    // Registration Modal States
    const [isRegistrationOpen, setIsRegistrationOpen] = useState(false);
    const [registrationData, setRegistrationData] = useState({
        username: '',
        password: '',
        confirmPassword: '',
        email: '',
        firstName: '',
        lastName: '',
        phoneNumber: ''
    });
    const [registrationMessage, setRegistrationMessage] = useState({ type: '', content: '' });

     // Login Modal States
    const [isLoginOpen, setIsLoginOpen] = useState(false);
    const [loginData, setLoginData] = useState({
        username: '',
        password: ''
    });
    const [loginMessage, setLoginMessage] = useState({ type: '', content: '' });


    const handleMouseEnter = () => setIsHovered(true);
    const handleMouseLeave = () => setIsHovered(false);

    // Registration Modal Methods
    const openRegistrationModal = () => setIsRegistrationOpen(true);
    const closeRegistrationModal = () => {
        setIsRegistrationOpen(false);
        setRegistrationMessage({ type: '', content: '' });
    };

    const handleRegistrationChange = (e) => {
        setRegistrationData({ 
            ...registrationData, 
            [e.target.name]: e.target.value 
        });
    };

    const handleRegistrationSubmit = async (e) => {
        e.preventDefault();
        
        // Basic Validation
        if (registrationData.password !== registrationData.confirmPassword) {
            setRegistrationMessage({ 
                type: 'error', 
                content: 'Passwords do not match' 
            });
            toast.error('Passwords do not match');
            return;
        }

        try {
            const response = await axios.post('http://localhost:8081/admin/register', registrationData);
            setRegistrationMessage({ 
                type: 'success', 
                content: response.data 
            });
            toast.success(response.data);
            closeRegistrationModal();
        } catch (error) {
            const errorMsg = error.response?.data?.message || 'Error registering admin';
            setRegistrationMessage({ 
                type: 'error', 
                content: errorMsg 
            });
            toast.error(errorMsg);
        }
    };
    // Login Modal Methods
        const openLoginModal = () => setIsLoginOpen(true);
        const closeLoginModal = () => {
            setIsLoginOpen(false);
            setLoginMessage({ type: '', content: '' });
        };

        const handleLoginChange = (e) => {
            setLoginData({
                ...loginData,
                [e.target.name]: e.target.value
            });
        };

        const handleLoginSubmit = async (e) => {
            e.preventDefault();

            try {
                const response = await axios.post('http://localhost:8081/admin/login', loginData);
                setLoginMessage({
                    type: 'success',
                    content: response.data.message
                });
                toast.success(response.data.message);
                closeLoginModal();
                navigate('/dashboard'); // Navigate to dashboard after successful login
            } catch (error) {
                const errorMsg = error.response?.data?.message || 'Error logging in';
                setLoginMessage({
                    type: 'error',
                    content: errorMsg
                });
                toast.error(errorMsg);
            }
        };

    useEffect(() => {
        const dishInterval = setInterval(() => {
            setCurrentDish(dishes[(dishes.indexOf(currentDish) + 1) % dishes.length]);
        }, 1000);

        const imageInterval = setInterval(() => {
            setCurrentImageIndex((currentImageIndex + 1) % imageGallery.length);
        }, 1000);

        const countInterval = setInterval(() => {
            setCustomerCount(prevCount => prevCount + 1);
        }, 120000);

        return () => {
            clearInterval(dishInterval);
            clearInterval(imageInterval);
            clearInterval(countInterval);
        };
    }, [currentDish, currentImageIndex]);

    return (
        <div className="App">
            <ToastContainer />
            
            {/* Registration Modal */}
      {isRegistrationOpen && (
        <div className="modal-overlay" style={{
          position: 'fixed',
          top: 0,
          left: 0,
          width: '100%',
          height: '100%',
          backgroundColor: 'rgba(0,0,0,0.5)',
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          zIndex: 1000
        }}>
          <div className="modal-content" style={{
            backgroundColor: 'white',
            padding: '30px',
            borderRadius: '15px',
            width: '650px',
            // Remove fixed height for better responsiveness
            // height: '650px', 
            position: 'relative',
            boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
          }}>
            <button
              onClick={closeRegistrationModal}
              style={{
                position: 'absolute',
                top: '15px',
                right: '15px',
                background: 'none',
                border: 'none',
                fontSize: '20px',
                cursor: 'pointer',
                color: '#888'
              }}
            >
              <FontAwesomeIcon icon={faTimes} />
            </button>
            <h2 style={{ textAlign: 'left', marginBottom: '25px', color: '#333', fontWeight: 'bold'  }}>
              Create your account
            </h2>
            <form onSubmit={handleRegistrationSubmit}>
              <div className="form-fields">
                
              </div>
              <div className="phone-username">
                <input 
                  type="text" 
                  name="firstName" 
                  placeholder="First Name"
                  value={registrationData.firstName}
                  onChange={handleRegistrationChange} 
                  style={{
                    width: '100%',
                    padding: '10px',
                    borderRadius: '5px',
                    border: '1px solid #ddd'
                  }}
                  required 
                />
                <input 
                  type="text" 
                  name="lastName" 
                  placeholder="Last Name"
                  value={registrationData.lastName}
                  onChange={handleRegistrationChange} 
                  style={{
                    width: '100%',
                    padding: '10px',
                    borderRadius: '5px',
                    border: '1px solid #ddd'
                  }}
                  required 
                />
                <input 
                  type="text" 
                  name="phoneNumber" 
                  placeholder="Phone Number"
                  value={registrationData.phoneNumber}
                  onChange={handleRegistrationChange} 
                  style={{
                    width: '100%',
                    padding: '10px',
                    borderRadius: '5px',
                    border: '1px solid #ddd'
                  }}
                  required 
                />
                <input 
                  type="text" 
                  name="username" 
                  placeholder="Username"
                  value={registrationData.username}
                  onChange={handleRegistrationChange} 
                  style={{
                    width: '100%',
                    padding: '10px',
                    borderRadius: '5px',
                    border: '1px solid #ddd'
                  }}
                  required 
                />
                <input 
                  type="email" 
                  name="email" 
                  placeholder="Email Address"
                  value={registrationData.email}
                  onChange={handleRegistrationChange} 
                  style={{
                    width: '100%',
                    padding: '10px',
                    borderRadius: '5px',
                    border: '1px solid #ddd'
                  }}
                  required 
                />
                <input 
                  type="password" 
                  name="password" 
                  placeholder="Password"
                  value={registrationData.password}
                  onChange={handleRegistrationChange} 
                  style={{
                    width: '100%',
                    padding: '10px',
                    borderRadius: '5px',
                    border: '1px solid #ddd'
                  }}
                  required 
                />
                <input 
                  type="password" 
                  name="confirmPassword" 
                  placeholder="Confirm Password"
                  value={registrationData.confirmPassword}
                  onChange={handleRegistrationChange} 
                  style={{
                    width: '100%',
                    padding: '10px',
                    borderRadius: '5px',
                    border: '1px solid #ddd'
                  }}
                  required 
                />
              </div>
              {registrationMessage.content && (
                <div style={{
                  marginTop: '15px',
                  color: registrationMessage.type === 'error' ? 'red' : 'green',
                  textAlign: 'center'
                }}>
                  {registrationMessage.content}
                </div>
              )}

              <button 
                type="submit" 
                style={{
                  width: '50%',
                  padding: '12px',
                  marginTop: '20px',
                  backgroundColor: 'blanchedalmond',
                  border: 'none',
                  borderRadius: '10px',
                  fontWeight: 'bold',
                  cursor: 'pointer',
                }}><span style={{
                    background: 'linear-gradient(to right, blue, purple)',
                    backgroundClip: 'text',
                    webkitBackgroundClip: 'text',
                    color: 'transparent',
                  }}>Create</span>
                  <span style={{
                    background: 'linear-gradient(to right, purple, red)',
                    backgroundClip: 'text',
                    webkitBackgroundClip: 'text',
                    color: 'transparent',
                  }}> Account </span>
              </button>
            </form>
                    </div>
                </div>
            )}
            {/* Login Modal */}
                        {isLoginOpen && (
                            <div className="modal-overlay" style={{
                                position: 'fixed',
                                top: 0,
                                left: 0,
                                width: '100%',
                                height: '100%',
                                backgroundColor: 'rgba(0,0,0,0.5)',
                                display: 'flex',
                                justifyContent: 'center',
                                alignItems: 'center',
                                zIndex: 1000
                            }}>
                                <div className="modal-content" style={{
                                    backgroundColor: 'white',
                                    padding: '30px',
                                    borderRadius: '15px',
                                    width: '500px',
                                    position: 'relative',
                                    boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
                                }}>
                                    <button
                                        onClick={closeLoginModal}
                                        style={{
                                            position: 'absolute',
                                            top: '15px',
                                            right: '15px',
                                            background: 'none',
                                            border: 'none',
                                            fontSize: '20px',
                                            cursor: 'pointer',
                                            color: '#888'
                                        }}
                                    >
                                        <FontAwesomeIcon icon={faTimes} />
                                    </button>
                                    <h2 style={{ textAlign: 'left', marginBottom: '25px', color: '#333', fontWeight: 'bold' }}>
                                        Login to your account
                                    </h2>
                                    <form onSubmit={handleLoginSubmit}>
                                        <div className="login-fields">
                                            <input
                                                type="text"
                                                name="username"
                                                placeholder="Username"
                                                value={loginData.username}
                                                onChange={handleLoginChange}
                                                style={{
                                                    width: '100%',
                                                    padding: '10px',
                                                    marginBottom: '15px',
                                                    borderRadius: '5px',
                                                    border: '1px solid #ddd'
                                                }}
                                                required
                                            />
                                            <input
                                                type="password"
                                                name="password"
                                                placeholder="Password"
                                                value={loginData.password}
                                                onChange={handleLoginChange}
                                                style={{
                                                    width: '100%',
                                                    padding: '10px',
                                                    marginBottom: '15px',
                                                    borderRadius: '5px',
                                                    border: '1px solid #ddd'
                                                }}
                                                required
                                            />
                                        </div>
                                        {loginMessage.content && (
                                            <div style={{
                                                marginTop: '15px',
                                                color: loginMessage.type === 'error' ? 'red' : 'green',
                                                textAlign: 'center'
                                            }}>
                                                {loginMessage.content}
                                            </div>
                                        )}

                                        <button
                                            type="submit"
                                            style={{
                                                width: '50%',
                                                padding: '12px',
                                                marginTop: '20px',
                                                backgroundColor: 'blanchedalmond',
                                                border: 'none',
                                                borderRadius: '10px',
                                                fontWeight: 'bold',
                                                cursor: 'pointer',
                                            }}
                                        >
                                            <span style={{
                                                background: 'linear-gradient(to right, blue, purple)',
                                                backgroundClip: 'text',
                                                webkitBackgroundClip: 'text',
                                                color: 'transparent',
                                            }}>Login</span>
                                        </button>
                                    </form>
                                </div>
                            </div>
                        )}

            <header className="header">
                <div className="logo">
                    <img alt="Patil Dhaba Logo" src={LogoImage} />
                    <span style={{ color: '#ffcc00', fontSize: '24px', fontWeight: 'bold' }}>
                        Patil Dhaba
                    </span>
                </div>
                <div className="nav">
                    <Link to="" className="nav-link"onClick={openRegistrationModal}>Register</Link>
                     <Link to="" className="nav-link" onClick={openLoginModal}>Login</Link>
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