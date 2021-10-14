import * as React from 'react';
import * as axios from 'axios';
import './App.css';
import {Auth0Provider, Auth0Context, Auth0ContextInterface, User} from '@auth0/auth0-react';
import {LoginButton} from './user/components/LoginButton';
import {LogoutButton} from './user/components/LogoutButton';

export default class App extends React.Component {

  public render(): React.ReactNode {
    return (
      <Auth0Provider
        domain='dev-tt4we0ft.us.auth0.com'
        clientId='GlzgXiWMhSPvu0DRBo1jAaYIMmRSEf9r'
        audience='chess-api'
        redirectUri={window.location.origin}
        cacheLocation='localstorage'
      >
        
          <Auth0Context.Consumer>
            {(user: Auth0ContextInterface) => (
              <div className='app-container'>
                <div className='app-header'>
                  {user.isAuthenticated ? <LogoutButton/> : <LoginButton/>}
                </div>
                <div className='app-body'>
                  <pre>{JSON.stringify(user, null, 2)}</pre>
                  <button onClick={() => this.getUser(user)}>Get Token</button>
                </div>
              </div>
            )}
          </Auth0Context.Consumer>
       
      </Auth0Provider>
    );
  } 

  private async getUser(user: Auth0ContextInterface<User>): Promise<void> {
    const token = await user.getAccessTokenSilently();
    const response = axios.default.get('http://localhost:8080/api/v1/user/', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log({response});
  }
}

