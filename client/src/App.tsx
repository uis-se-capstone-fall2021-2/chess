import {autobind} from 'core-decorators';
import * as React from 'react';
import * as axios from 'axios';
import './App.css';
import {Auth0Provider, Auth0Context, Auth0ContextInterface, User} from '@auth0/auth0-react';
import {LoginButton} from './user/components/LoginButton';
import {LogoutButton} from './user/components/LogoutButton';

@autobind
export class App extends React.Component<{}, {token: string|null, claims: any}> {

  public state = {
    token: null,
    claims: null
  };

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
                  {this.renderToken()}
                  {this.renderSubject()}
                </div>
              </div>
            )}
          </Auth0Context.Consumer>
       
      </Auth0Provider>
    );
  }

  private renderToken(): React.ReactNode {
    if(!this.state.token) {
      return null;
    }
    return (
      <div>
        <h4>Token</h4>
        <pre>{this.state.token}</pre>
        <button onClick={this.copyTokenToClipboard}>Copy</button>
      </div>
    );
  }

  private copyTokenToClipboard() {
    navigator.clipboard.writeText(this.state.token ?? '');
  }

  private renderSubject(): React.ReactNode {
    if(!this.state.claims) {
      return null;
    }
    return (
      <div>
        <h4>Subject</h4>
        <pre>{JSON.stringify(this.state.claims, null, 2)}</pre>
      </div>
    );
  }

  private async getUser(user: Auth0ContextInterface<User>): Promise<void> {
    const token = await user.getAccessTokenSilently();
    this.setState({token});
    const claims = await axios.default.get('http://localhost:8080/api/v1/user/whoami', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    const userInfo = await axios.default.get(`${(claims.data as any).iss}userinfo`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    this.setState({claims: userInfo.data});
    
  }
}

