import * as React from 'react';
import './App.css';
import {Auth0Provider, Auth0Context, Auth0ContextInterface, User} from '@auth0/auth0-react';
import {LoginButton} from './user/components/LoginButton';
import {LogoutButton} from './user/components/LogoutButton';
import {AppBar, Box, Button, Container, IconButton, Toolbar, Typography} from '@mui/material';
import {Menu as MenuIcon} from '@mui/icons-material';

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
              <Box sx={{flexGrow: 1}}>
                <AppBar>
                <Toolbar>
                  <IconButton
                    size="large"
                    edge="start"
                    color="inherit"
                    aria-label="menu"
                    sx={{ mr: 2 }}
                  >
                    <MenuIcon />
                  </IconButton>
                  <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    Chess
                  </Typography>
                  {user.isAuthenticated ? <LogoutButton/> : <LoginButton/>}
                </Toolbar>
                </AppBar>
                <Container>
                  <pre>{JSON.stringify(user, null, 2)}</pre>
                  <Button onClick={() => this.getToken(user)}>Get Token</Button>
                  <span>{`Token: ${this.state.token}`}</span>
                </Container>
              </Box>
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

