import {Auth0Context, Auth0ContextInterface, User} from '@auth0/auth0-react';
import {AppBar, Box, Button, Container, IconButton, ThemeProvider, Toolbar, Typography} from '@mui/material';
import {Menu as MenuIcon} from '@mui/icons-material';
import * as axios from 'axios';
import {autobind} from 'core-decorators';
import * as React from 'react';

import {LogoutButton} from '../../user/components/LogoutButton';
import {theme} from './theme';

@autobind
export class App extends React.Component<{}, {token: string|null, userInfo: object|null}> {

  public state = {
    token: null,
    userInfo: null,
    userName: null
  };

  public render(): React.ReactNode {
    return (
      <ThemeProvider theme={theme}>
        <Auth0Context.Consumer>
          {(user: Auth0ContextInterface) => (
            <Box>
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
                  <LogoutButton/>
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
      </ThemeProvider>
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

