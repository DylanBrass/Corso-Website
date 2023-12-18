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
    <div className="dropdown-container border-0">
      <div className="dropdown-header border-0" onClick={toggleDropdown}>
        {label}
        <span className="dropdown-symbol border-0">{isOpen ? '▲' : '▼'}</span>
      </div>
      {isOpen && (
        <ul className="dropdown-list border-0">

          {
            // @ts-ignore
            options.map((option, index) => (
            <li
              key={index}
              className={"dropdown-option border-0 " + option === selectedOption ? 'selected' : ''}
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