import React, { useState, useEffect } from 'react';
import './DateInput.css';

function DateInput(props) {
  const {
    isLoading,
    onSubmit,
  } = props;

  const [date, setDate] = useState('');
  const [hasDate, setHasDate] = useState(false);

  useEffect(() => {
    setHasDate(date !== '');
  }, [date]);

  function onDateChange(e) {
    setDate(e.target.value);
  }

  function onButtonClick(e) {
    if (hasDate && !isLoading) {
      onSubmit(date);
    }
  }

  function onKeyPress(e) {
    if (e.key === "Enter") {
      onSubmit(date);
    }
  }

  const baseClassName = 'DateInput';
  var inputClassName = `${baseClassName}__input`;
  var buttonClassName = `${baseClassName}__button`;
  var buttonText = 'Load Image';
  if (isLoading) {
    inputClassName += '--disabled';
    buttonClassName += '--disabled';
    buttonText = 'Loading...';
  } else if (date === '') {
    buttonClassName += '--disabled';
  }

  return (
    <div className={baseClassName}>
      <input type="text" className={inputClassName} value={date} placeholder="Enter a Date" onChange={onDateChange} disabled={isLoading} onKeyPress={onKeyPress} />
      <button className={buttonClassName} onClick={onButtonClick} disabled={!hasDate || isLoading}>{buttonText}</button>
    </div>
  );
}

export default DateInput;
