import {GridRenderCellParams} from '@mui/x-data-grid';
import {autobind} from 'core-decorators';
import * as Strongbus from 'strongbus';
import * as React from 'react';

import {Inject} from '../../../di';
import {Player, PlayerService} from '../../../player/interfaces';
import {PlayerId} from '../../../types';

@autobind
export class PlayerCell extends React.Component<GridRenderCellParams<PlayerId>, PlayerCell.State> {

  @Inject(PlayerService.Token)
  private readonly playerService: PlayerService;
  private playerSubscription: Strongbus.Subscription;
  public override state: PlayerCell.State = {
    player: null
  };

  public override componentDidMount() {
    const {value: playerId} = this.props;
    this.playerSubscription = this.playerService.on(`PLAYER_UPDATED_${playerId}`, this.updatePlayer);
    this.updatePlayer();
  }

  public override componentWillUnmount() {
    this.playerSubscription?.();
  }
  
  private updatePlayer() {
    const {value: playerId} = this.props;
    const player = this.playerService.getPlayer(playerId);
    if(player) {
      this.setState({player});
    } else {
      this.playerService.fetchPlayer(playerId); // will trigger PLAYER_ADDED event, and we'll get back here
      return;
    }
  }

  public override render(): React.ReactNode {
    const {value: playerId} = this.props;
    const {player} = this.state;
    
    return player?.displayName ?? playerId;
  }
}

export namespace PlayerCell {
  export interface State {
    player: Player;
  }
}