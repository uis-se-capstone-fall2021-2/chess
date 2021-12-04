import {autobind} from 'core-decorators';
import * as Strongbus from 'strongbus';
import * as React from 'react';

import {Inject} from '../../di';
import {Player, PlayerId, PlayerService} from '../interfaces';

@autobind
export class PlayerProvider extends React.Component<PlayerProvider.Props, PlayerProvider.State> {

  @Inject(PlayerService.Token)
  private readonly playerService: PlayerService;
  private playerSubscription: Strongbus.Subscription;
  public override state: PlayerProvider.State = {
    player: null
  };

  public override componentDidMount() {
    const {playerId} = this.props;
    this.playerSubscription = this.playerService.on(`PLAYER_UPDATED_${playerId}`, this.updatePlayer);
    this.updatePlayer();
  }

  public override componentWillUnmount() {
    this.playerSubscription?.();
  }
  
  private updatePlayer() {
    const {playerId} = this.props;
    const player = this.playerService.getPlayer(playerId);
    if(player) {
      this.setState({player});
    } else {
      this.playerService.fetchPlayer(playerId); // will trigger PLAYER_ADDED event, and we'll get back here
      return;
    }
  }

  public override render(): React.ReactNode {
    const {children} = this.props;
    const {player} = this.state;
    
    return children(player);
  }
}

export namespace PlayerProvider {

  export interface Props {
    playerId: PlayerId;
    children: (player: Player) => React.ReactNode;
  }

  export interface State {
    player: Player;
  }
}