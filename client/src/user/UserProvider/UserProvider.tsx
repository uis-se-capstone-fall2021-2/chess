import * as axios from 'axios';
import {autobind} from 'core-decorators';
import * as React from 'react';
import {Auth0ContextInterface, User as Auth0User} from "@auth0/auth0-react";

import {User} from '../User';
import {Box} from '@mui/system';

import './style.css';


@autobind
export class UserProvider extends React.Component<UserProvider.Props, UserProvider.State> {

  public state: UserProvider.State = {
    user: null,
    error: null
  };

  public componentDidMount() {
    this.getUser(this.props.auth0userContext);
  }

  public render(): React.ReactNode {
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

  private async getUser(user: Auth0ContextInterface<Auth0User>): Promise<void> {
    const token = await user.getAccessTokenSilently();
    try {
      const {data: userInfo} = await axios.default.get<User>('http://localhost:8080/api/v1/user', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      this.setState({
        user: {
          ...userInfo,
          token
        }
      });
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