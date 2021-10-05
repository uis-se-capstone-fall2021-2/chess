import * as React from 'react';
import './App.css';
import {Auth0Provider, Auth0Context, Auth0ContextInterface, User} from '@auth0/auth0-react';
import {LoginButton} from './user/components/LoginButton';
import {LogoutButton} from './user/components/LogoutButton';

export default class App extends React.Component<{}, {token: string|null}> {

  public state = {token: null};

  public render(): React.ReactNode {
    return (
      <Auth0Provider
        domain='dev-tt4we0ft.us.auth0.com'
        clientId='GlzgXiWMhSPvu0DRBo1jAaYIMmRSEf9r'
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
                  <button onClick={() => this.getToken(user)}>Get Token</button>
                  <span>{`Token: ${this.state.token}`}</span>
                </div>
              </div>
            )}
          </Auth0Context.Consumer>
       
      </Auth0Provider>
    );
  } 

  private async getToken(user: Auth0ContextInterface<User>): Promise<void> {
    const token = await user.getAccessTokenSilently();
    this.setState({token});
  }
}

