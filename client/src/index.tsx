import 'reflect-metadata';
import {Container} from 'typedi';
import * as React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import {AppContainer} from './entry/AppContainer';
import reportWebVitals from './reportWebVitals';
import {Tokens} from './di';
import axios, {AxiosError} from 'axios';

document.title = "King's Folly";


// TODO: load this from environment
Container.set(Tokens.API_HOST, process.env.REACT_APP_API_HOST);
Container.set(Tokens.STOMP_BROKER_BASE_URL, process.env.REACT_APP_STOMP_BROKER_BASE_URL);
Container.set(Tokens.AUTH_0_CLIENT_ID, process.env.REACT_APP_AUTH_0_CLIENT_ID);

axios.interceptors.response.use(
  value => value,
  error => {
    const msg = (error as AxiosError<{message: string}>)?.response?.data?.message;
    return Promise.reject(msg ? new Error(msg) : error);
  }
);

// (window as any).di = Container;

ReactDOM.render(
  <React.StrictMode>
    <AppContainer />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
