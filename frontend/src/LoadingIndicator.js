import React from 'react';

import './LoadingIndicator.css';
import { ReactComponent as Spinner } from './spinner.svg';

function LoadingIndicator() {
  return (
    <div className="LoadingIndicator">
      <Spinner className="LoadingIndicator__Spinner" />
    </div>
  );
}

export default LoadingIndicator;
