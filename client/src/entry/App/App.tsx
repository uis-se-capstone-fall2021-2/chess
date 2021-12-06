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
  Add as AddIcon,
  GridOn as GridOnIcon,
  Menu as MenuIcon
} from '@mui/icons-material';
import {autobind} from 'core-decorators';
import PopupState, {bindTrigger, bindMenu} from 'material-ui-popup-state';
import * as React from 'react';
import {
  HashRouter as Router,
  Switch,
  Route,
  Link
} from 'react-router-dom';

import '../../game/impl/GameService';
import '../../game/impl/GameStore';
import '../../messaging/impl/MessagingService';
import '../../player/impl/PlayerService';
import '../../player/impl/PlayerStore';
import '../../utils/resource/ResourceFactory.impl';

import {RectContext} from '../../utils/layout/RectContext';
import {User} from '../../user/interfaces';
import {UserProvider} from '../../user/components/UserProvider';
import {Home} from '../../views/Home';
import {GameView} from '../../views/games/Game';
import {MyGames} from '../../views/games/MyGames';
import {theme} from './theme';

import './style.css';



@autobind
export class App extends React.Component<{}, {
  mobileNavOpen: boolean
}> {

  private static readonly NAV_WIDTH: number = 240;

  public override state: App.State = {
    mobileNavOpen: false
  }

  public override render(): React.ReactNode {
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
                    <Box
                      component='nav'
                      sx={{
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
                        padding: {
                          xs: '4px'
                        },
                        width: {
                          xs: '100%',
                          sm: `calc(100% - ${App.NAV_WIDTH}px)`
                        },
                        ml: {
                          xs: 0,
                          sm: `${App.NAV_WIDTH}px`
                        },
                        p: 3
                      }}
                    > 
                      <Toolbar/>
                      <Box sx={{flex: '1 1'}}>
                        <RectContext.Observer>
                          <Switch>
                            <Route exact path='/'>
                              <Home/>
                            </Route>
                            <Route path='/games'>
                              {(ctx) => (
                                <Switch>
                                  <Route exact path={`${ctx.match?.path}/:gameId([\\d]+)`}>
                                    {(ctx) => (<GameView gameId={parseInt(ctx.match?.params?.gameId)}/>)}
                                  </Route>
                                  <Route>
                                    <MyGames/>
                                  </Route>
                                </Switch>
                                )}
                            </Route>
                          </Switch>
                        </RectContext.Observer>
                      </Box>
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
            <Link to='/' onClick={this.closeMobileNav}>
              <ListItemIcon>
                <AddIcon sx={this.navSx}/>
              </ListItemIcon>
              <ListItemText primary='New Game'/>
            </Link>
          </ListItem>
          <ListItem button>
            <Link to="/games/active" onClick={this.closeMobileNav}>
              <ListItemIcon>
                <GridOnIcon sx={this.navSx}/>
              </ListItemIcon>
              <ListItemText primary='My Games'/>
            </Link>
          </ListItem>
          <Divider/>
          <List sx={{paddingLeft: '60px'}}>
            <ListItem button>
              <Link to='/games/active' onClick={this.closeMobileNav}>
                <ListItemText primary='Active'/>
              </Link>
            </ListItem>
            <ListItem button>
              <Link to='/games/pending' onClick={this.closeMobileNav}>
                <ListItemText primary='Pending'/>
              </Link>
            </ListItem>
            <ListItem button>
              <Link to='/games/history' onClick={this.closeMobileNav}>
                <ListItemText primary='History'/>
              </Link>
            </ListItem>
          </List>
          <Divider/>
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

  private closeMobileNav(): void {
    this.setState({mobileNavOpen: false});
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

