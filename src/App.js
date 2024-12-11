import React, { useState } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import AdminRegister from './Components/AdminRegister';
import AdminLogin from './Components/AdminLogin';
import Homepage from './Components/Homepage';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Menu from './Components/Menu';
import Dashboard from './Components/Dashboard'; // Ensure the correct path to your Dashboard component
import UpdateProfile from './Components/UpdateProfile';
import EmployeeManagement from './Components/EmployeeManagement';


function App() {
  const [showRegisterModal, setShowRegisterModal] = useState(false);
  const [showLoginModal, setShowLoginModal] = useState(false);

  const handleRegisterClick = () => {
    setShowRegisterModal(true);
  };

  const handleLoginClick = () => {
    setShowLoginModal(true);
  };

  return (
    <BrowserRouter>
      <div className="App">
        <Routes>
          <Route path="/register" element={<AdminRegister />} />
          <Route path="/login" element={<AdminLogin />} />
          <Route path="/menu" element={<Menu />} />
          <Route path="/dashboard" element={<Dashboard />} /> {/* Fixed the component name */}
          <Route path="/update-profile" element={<UpdateProfile />} />
          <Route path="/employees" element={<EmployeeManagement />} />
          <Route
            path="/"
            element={<Homepage handleRegisterClick={handleRegisterClick} handleLoginClick={handleLoginClick} />}
          />
        </Routes>
        <ToastContainer />
      </div>
    </BrowserRouter>
  );
}

export default App;