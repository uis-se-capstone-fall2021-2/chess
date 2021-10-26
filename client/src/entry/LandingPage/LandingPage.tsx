import {Box} from '@mui/material';

import {LoginButton} from '../../user/components/LoginButton';

import './style.css';

export function LandingPage() {
  return (
    <Box sx={{flexGrow: 1}} className='landing-page'>
      <LoginButton/>
    </Box>
  );
}