import {autobind} from 'core-decorators';
import * as React from 'react';

import {PlayerId} from '../../types';
import {Inject} from '../../di';
import {GameService} from '../../game/interfaces';
import {User} from '../../user/interfaces';

@autobind