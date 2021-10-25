import {autobind} from 'core-decorators';
import * as React from 'react';
import * as axios from 'axios';
import './App.css';
import {Auth0Provider, Auth0Context, Auth0ContextInterface, User} from '@auth0/auth0-react';
import {LoginButton} from './user/components/LoginButton';
import {LogoutButton} from './user/components/LogoutButton';
import {AppBar, Box, Button, Container, IconButton, Toolbar, Typography} from '@mui/material';
import {Menu as MenuIcon} from '@mui/icons-material';

@autobind
export class App extends React.Component<{}, {token: string|null, userInfo: object|null}> {

  public state = {
    token: null,
    userInfo: null,
    userName: null
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
                  <Button onClick={() => this.getUser(user)}>Get Token</Button>
                  {this.renderToken()}
                  {this.renderUser()}
                </Container>
              </Box>
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

  private renderUser(): React.ReactNode {
    if(!this.state.userInfo) {
      return null;
    }
    return (
      <div>
        <h4>{this.state.userName}</h4>
        <pre>{JSON.stringify(this.state.userInfo, null, 2)}</pre>
      </div>
    );
  }

  private async getUser(user: Auth0ContextInterface<User>): Promise<void> {
    const token = await user.getAccessTokenSilently();
    this.setState({token});
    try {
      const {data: userInfo} = await axios.default.get('http://localhost:8080/api/v1/user', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      this.setState({userInfo});
    } catch(e) {

    }
    
  }
}

