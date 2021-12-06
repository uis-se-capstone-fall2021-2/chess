import {autobind} from 'core-decorators';
import * as React from 'react';
import * as Strongbus from 'strongbus';

import {Inject} from '../../di';
import {GameService, GameData, GameId} from '../interfaces';


export class GameProvider extends React.Component<GameProvider.Props> {
  public override render(): React.ReactNode {
    return (
      <GameProviderInner key={this.props.gameId} {...this.props}/>
    );
  }
}

@autobind
class GameProviderInner extends React.Component<GameProvider.Props, GameProvider.State> {
  private __mounted: boolean = false;
  private gameSubscription: Strongbus.Subscription;
  @Inject(GameService.Token)
  private readonly gameService: GameService;

  constructor(props: GameProvider.Props, context: any) {
    super(props, context);

    this.state = {
      game: this.gameService.getGame(props.gameId),
      error: null
    };
  }

  public override async componentDidMount() {
    this.__mounted = true;
    this.gameSubscription = this.gameService.subscribe(this.props.gameId, this.updateGame);
  }

  public override componentWillUnmount() {
    this.gameSubscription?.();
    this.__mounted = false;
  }

  private updateGame(): void {
    if(!this.__mounted) {
      return;
    }

    const game = this.gameService.getGame(this.props.gameId);
    this.setState({
      error: null,
      game
    });
  }

  public override render(): React.ReactNode {
    const {children, errorRenderer} = this.props;
    const {error, game} = this.state;

    if(error && errorRenderer) {
      return errorRenderer(error.message);
    } else if(!game) {
      return null;
    } else {
      return children(game);
    }
  }

}

export namespace GameProvider {
  export interface Props {
    gameId: GameId;
    children: (game: GameData) => React.ReactNode;
    errorRenderer?: (message: string) => React.ReactNode;
  }

  export interface State {
    game: GameData|null;
    error: Error|null;
  }
}