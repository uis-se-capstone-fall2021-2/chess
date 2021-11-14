import {Auth0Provider, Auth0Context, Auth0ContextInterface} from '@auth0/auth0-react';
import {Box} from '@mui/system';

import {App} from './App';
import {LandingPage} from './LandingPage';

import './style.css';

export function AppContainer(): React.ReactElement {
  return (
    <Box sx={{
      display: 'flex',
      flex: '1 1 auto',
      height: '100%',
      width: '100%'
    }}>
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
    </Box>
  );
}
