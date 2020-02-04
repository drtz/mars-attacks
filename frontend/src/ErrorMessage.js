import React from 'react';

import './ErrorMessage.css';

function ErrorMessage({ message }) {
  return <div className="ErrorMessage">{message}</div>;
}

export default ErrorMessage;
