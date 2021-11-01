import {Auth0ContextInterface, User as Auth0User} from "@auth0/auth0-react";
import {Box} from '@mui/system';
import axios from 'axios';
import {autobind} from 'core-decorators';
import * as React from 'react';
import {Container} from 'typedi';

import {Inject, Tokens} from '../../di';
import {User} from '../User';

import './style.css';


@autobind
export class UserProvider extends React.Component<UserProvider.Props, UserProvider.State> {

  @Inject(Tokens.API_HOST)
  private readonly apiHost: string;

  public override state: UserProvider.State = {
    user: null,
    error: null
  };

  public override componentDidMount() {
    this.loadUser(this.props.auth0userContext);
  }

  public override render(): React.ReactNode {
    const {user, error} = this.state;
    if(error) {
      return (
        <Box sx={{ color: 'error.main' }}>
          <h2>{error.toString()}</h2>
        </Box>
      );
    } else if(user) {
      return this.props.children(user);
    } else {
      return (
        <Box className='user-provider loading'>
          <h4>Loading...</h4>
        </Box>
      );
    }
  }

  private async loadUser(auth0user: Auth0ContextInterface<Auth0User>): Promise<void> {
    const token = await auth0user.getAccessTokenSilently();
    try {
      const {data: userInfo} = await axios.get<User>(`${this.apiHost}/api/v1/user`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      const user: User = {
        ...userInfo,
        token
      }
      if(!Container.has(User.Token)) {
        Container.set(User.Token, user);
      }
      this.setState({user});
    } catch(e) {
      this.setState({error: e as Error});
    }
  }
}

export namespace UserProvider {
  export interface Props {
    auth0userContext: Auth0ContextInterface<Auth0User>;
    children: (user: User) => React.ReactNode;
  }
  export interface State {
    user: User|null;
    error: Error|null;
  }
}