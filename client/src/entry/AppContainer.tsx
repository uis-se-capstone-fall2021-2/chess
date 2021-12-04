import {Auth0Provider, Auth0Context, Auth0ContextInterface} from '@auth0/auth0-react';
import {Box} from '@mui/system';
import * as React from 'react';

import {App} from './App';
import {LandingPage} from './LandingPage';
import {Inject, Tokens} from '../di';

import './style.css';

export class AppContainer extends React.Component {

  @Inject(Tokens.AUTH_0_CLIENT_ID)
  private readonly auth0ClientId: string;

  public override render(): React.ReactNode {
    return (
      <Box sx={{
        display: 'flex',
        flex: '1 1 auto',
        height: '100%',
        width: '100%'
      }}>
        <Auth0Provider
          domain='dev-tt4we0ft.us.auth0.com'
          clientId={this.auth0ClientId}
          audience='chess-api'
          redirectUri={window.location.origin}
          cacheLocation='localstorage'
        >
          <Auth0Context.Consumer>
            {(user: Auth0ContextInterface) => (
              user.isAuthenticated ? <App/> : <LandingPage/>
            )}
          </Auth0Context.Consumer>
        </Auth0Provider>
      </Box>
    );
  }
}

