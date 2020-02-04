import React from 'react';

import './Image.css';

function Image({ date, onLoad, onError, isHidden }) {
  if (date === '') {
    return null;
  }

  var className = "Image";
  if (isHidden) {
    className += '--hidden';
  }

  return (
    <img
      className={className}
      src={`/photos/?date=${date}`}
      alt="NASA Mars Rover"
      onLoad={onLoad}
      onError={onError}
    />
  );
}

export default Image;
