import React from 'react';
import { useNavigate } from 'react-router-dom';

const Button = ({ text, path }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(path);
  };

  return (
    <button className="btn btn-primary" onClick={handleClick}>{text}</button>
  );
};

export default Button;