import React, { useState, useEffect } from 'react';
import './App.css';
import Image from './Image.js';
import DateInput from './DateInput.js';
import ErrorMessage from './ErrorMessage.js';
import LoadingIndicator from './LoadingIndicator.js';

function App() {
  const [date, setDate] = useState('');
  const [errorMessage, setErrorMessage] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const onSubmit = (newDate) => {
    setDate(newDate);
  };

  useEffect(() => {
    if (date !== '') {
      setIsLoading(true);
      setErrorMessage(null);
    }
  }, [date]);

  const onImageLoad = () => {
    setIsLoading(false);
    setErrorMessage(null);
  };

  const onImageError = (e, f) => {
    setIsLoading(false);
    setErrorMessage(`Couldn't find an image for '${date}'`);
    console.log(e);
    debugger;
  };

  const isImageHidden = isLoading || date === '' || errorMessage !== null;

  return (
    <div className="App">
      <DateInput onSubmit={onSubmit} isLoading={isLoading} />
      <Image date={date} onLoad={onImageLoad} onError={onImageError} isLoading={isLoading} isHidden={isImageHidden} />
      {
        isLoading ?
          <LoadingIndicator />
        : errorMessage !== null ?
          <ErrorMessage message={errorMessage} />
        : null
      }
    </div>
  );
}

export default App;
