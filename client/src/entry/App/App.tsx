import {Auth0Context, Auth0ContextInterface} from '@auth0/auth0-react';
import {
  AppBar,
  Box,
  Button,
  CssBaseline,
  Divider,
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  IconButton,
  Menu,
  MenuItem,
  ThemeProvider,
  Toolbar,
  Typography
} from '@mui/material';
import {
  AccountCircleOutlined as AccountCircleOutlinedIcon,
  Gradient as GradientIcon,
  Menu as MenuIcon,
  QueryStats as QueryStatsIcon
} from '@mui/icons-material';
import {autobind} from 'core-decorators';
import PopupState, {bindTrigger, bindMenu} from 'material-ui-popup-state';
import * as React from 'react';
import {__RouterContext as RouterContext} from 'react-router';
import {
  HashRouter as Router,
  Switch,
  Route,
  Link
} from 'react-router-dom';

import {User} from '../../user/User';
import {UserProvider} from '../../user/UserProvider';
import {theme} from './theme';

import './style.css';


@autobind
export class App extends React.Component<{}, {
  mobileNavOpen: boolean
}> {

  private static readonly NAV_WIDTH: number = 240;

  public state: App.State = {
    mobileNavOpen: false
  }

  public render(): React.ReactNode {
    return (
      <ThemeProvider theme={theme}>
        <Auth0Context.Consumer>
          {(auth0User: Auth0ContextInterface) => (
            <UserProvider auth0userContext={auth0User}>
              {(appUser) => (
                <Router>
                  <Box className='app' sx={{display: 'flex'}}>
                    <CssBaseline/>
                    <AppBar position='fixed'>
                      <Toolbar>
                        <IconButton
                          onClick={this.toggleMobileNavVisibility}
                          size="large"
                          edge="start"
                          color="inherit"
                          aria-label="menu"
                          sx={{
                            mr: 2,
                            display: {
                              sm: 'none'
                            }
                          }}
                        >
                          <MenuIcon />
                        </IconButton>
                        {this.appTitle}
                        {this.userMenu({auth0User, appUser})}
                      </Toolbar>
                    </AppBar>
                    <Box component='nav' sx={{
                      width: {
                        sm: App.NAV_WIDTH
                      },
                      flexShrink: {
                        sm: 0
                      }
                    }}>
                      {this.mobileNavDrawer}
                      {this.desktopNavDrawer}
                    </Box>
                    <Box
                      component='main'
                      sx={{
                        backgroundColor: 'secondary.main',
                        width: {sm: `calc(100% - ${App.NAV_WIDTH}px)`},
                        ml: {sm: `${App.NAV_WIDTH}px`},
                        p: 3
                      }}
                    > 
                      <Toolbar/>
                      <Switch>
                        <Route exact path='/'>
                          <div>Home</div>
                        </Route>
                        <Route path='/games'>
                          <RouterContext.Consumer>
                            {(ctx) => (
                              <Switch>
                                <Route exact path={`${ctx.match.path}/active`}>
                                  <div>Active Games Here</div>
                                </Route>
                                <Route exact path={`${ctx.match.path}/history`}>
                                  <div>Game History Here</div>
                                </Route>
                                <Route exact path={`${ctx.match.path}/:gameId`}>

                                  <RouterContext.Consumer>
                                    {(ctx) => (
                                      <div>{`Chess Game id=${(ctx.match.params as any)?.gameId} here`}</div>
                                    )}
                                  </RouterContext.Consumer>
                                </Route>
                              </Switch>
                            )}
                          </RouterContext.Consumer>
                          
                        </Route>
                      </Switch>
                    </Box>
                  </Box>
                </Router>
              )}
            </UserProvider>
          )}
        </Auth0Context.Consumer>
      </ThemeProvider>
    );
  }

  private get navContent(): React.ReactNode {
    return (
      <>
        {this.mobileNavHeader}
        {this.desktopNavHeader}
        <Divider/>
        <List>
          <ListItem button>
            <Link to="/games/active">
              <ListItemIcon>
                <GradientIcon sx={this.navSx}/>
              </ListItemIcon>
              <ListItemText primary='Active Games'/>
            </Link>
          </ListItem>
          <ListItem button>
            <Link to='/games/history'>
              <ListItemIcon>
                <QueryStatsIcon sx={this.navSx}/>
              </ListItemIcon>
              <ListItemText primary='Game History'/>
            </Link>
          </ListItem>
        </List>
      </>
    );
  }

  private get appTitle(): React.ReactNode {
    return (
      <Typography variant="h6" component="div" sx={{
        flexGrow: 1,
      }}>
        Chess
      </Typography>
    );
  }

  private get navSx(): object {
    return {
      backgroundColor: 'primary.light',
      color: 'primary.contrastText'
    };
  }

  private toggleMobileNavVisibility(): void {
    this.setState((state) => ({mobileNavOpen: !state.mobileNavOpen}));
  }

  private get mobileNavHeader(): React.ReactNode {
    return (
      <Toolbar sx={{
        display: {
            sm: 'none'
          }
        }}
      >
        {this.appTitle}
      </Toolbar>
    );
  }

  private get desktopNavHeader(): React.ReactNode {
    return (
      <Toolbar sx={{
        backgroundColor: 'primary.main',
        color: 'primary.contrastText',
        display: {
          xs: 'none',
          sm: 'flex'
        }
      }}
      >
        {this.appTitle}
      </Toolbar>
    );
  }

  private get mobileNavDrawer(): React.ReactNode {
    const {mobileNavOpen} = this.state;
    return (
      <Drawer
        container={window.document.body}
        variant="temporary"
        open={mobileNavOpen}
        onClose={this.toggleMobileNavVisibility}
        ModalProps={{
          keepMounted: true, // Better open performance on mobile.
        }}
        sx={{
          display: {xs: 'block', sm: 'none'},
          '& .MuiDrawer-paper': {boxSizing: 'border-box', width: App.NAV_WIDTH},
          ...this.navSx
        }}
        PaperProps={{
          sx: this.navSx
        }}
      >
        {this.navContent}
      </Drawer>
    );
  }

  private get desktopNavDrawer(): React.ReactNode {
    return (
      <Drawer
        variant="permanent"
        open
        sx={{
          display: {xs: 'none', sm: 'block'},
          '& .MuiDrawer-paper': {boxSizing: 'border-box', width: App.NAV_WIDTH}
        }}
        PaperProps={{
          sx: this.navSx
        }}
      >
        {this.navContent}
      </Drawer>
    );
  }

  private userMenu(params: {auth0User: Auth0ContextInterface, appUser: User}): React.ReactNode {
    const {auth0User, appUser} = params;
    return (
      <PopupState
        variant='popover'
      >
        {(popup) => (
          <>
            <Button color='inherit' {...bindTrigger(popup)}>
              <span style={{marginRight: 4}}>{appUser.displayName}</span><AccountCircleOutlinedIcon/>
            </Button>
            <Menu {...bindMenu(popup)}>
              <MenuItem onClick={() => navigator.clipboard.writeText(appUser.token)}>
                Copy Token
              </MenuItem>
              <MenuItem onClick={() => auth0User.logout()}>
                Log Out
              </MenuItem>
            </Menu>
          </>
        )}
      </PopupState>
    );
  }
}

export namespace App {
  export interface State {
    mobileNavOpen: boolean;
  }
}

