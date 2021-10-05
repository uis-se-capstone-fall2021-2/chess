import * as React from "react";
import {useAuth0} from "@auth0/auth0-react";
import {Button} from '@mui/material';

export function LoginButton() {
  const {loginWithRedirect} = useAuth0();

  return <Button color='inherit' onClick={() => loginWithRedirect()}>Log In</Button>;
};