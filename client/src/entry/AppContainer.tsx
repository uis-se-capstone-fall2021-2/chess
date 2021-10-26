import {Auth0Provider, Auth0Context, Auth0ContextInterface} from '@auth0/auth0-react';

import {App} from './App';
import {LandingPage} from './LandingPage';

export function AppContainer(): React.ReactElement {
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
          user.isAuthenticated ? <App/> : <LandingPage/>
        )}
      </Auth0Context.Consumer>
    </Auth0Provider>
  );
}

