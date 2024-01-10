import React, { useState } from 'react';
import './dropdown.css';
// @ts-ignore
const Dropdown = ({ label, options, onSelect }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedOption, setSelectedOption] = useState(null);

  const toggleDropdown = () => setIsOpen(!isOpen);

  // @ts-ignore
  const handleOptionClick = (option) => {
    setSelectedOption(option);
    onSelect(option);
    setIsOpen(false);
  };

  return (
    <div className="dropdown-container">
      <div className="dropdown-header " onClick={toggleDropdown}>
        {label}
        <span className="dropdown-symbol">{isOpen ? '▲' : '▼'}</span>
      </div>
      {isOpen && (
        <ul className="dropdown-list">

          {
            // @ts-ignore
            options.map((option, index) => (
            <li
              key={index}
              className={option === selectedOption ? 'selected dropdown-option' : 'dropdown-option'}
              onClick={() => handleOptionClick(option)}
            >
              {option}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default Dropdown;