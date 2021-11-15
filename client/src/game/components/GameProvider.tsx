import {autobind} from 'core-decorators';
import {sleep} from 'jaasync';
import {Typography} from '@mui/material';
import * as React from 'react';
import * as Strongbus from 'strongbus';

import {Inject} from '../../di';
import {GameService, GameData, GameStatus, GameId} from '../interfaces';

/**
 * @description container to abstract keeping game state up-to-date and providing it to child component tree
 * current naive implementation is to poll the game service every 30s
 * future implementation is to use a real time service (websocket) to only get updates as necessary
 */
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
      error: null,
      gameRemoved: false
    };
  }

  public override async componentDidMount() {
    this.__mounted = true;
 
    const {gameId} = this.props;
    this.gameSubscription = this.gameService.on([
      `GAME_UPDATED_${gameId}`,
      `GAME_REMOVED_${gameId}`
    ], this.updateGame);


    while(
      this.__mounted &&
      !this.state.gameRemoved &&
      ![GameStatus.DECLINED, GameStatus.COMPLETE, GameStatus.TERMINATED].includes(this.state.game?.status)
    ) {
      try {
        await this.gameService.fetchGameState(this.props.gameId);
      } catch(error) {
        if(this.__mounted) {
          this.setState({error: error as Error});
        }
      }
      await sleep(15_000);
    }
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
    this.setState((prevState) => ({
      error: null,
      game,
      gameRemoved: Boolean(prevState.game) && !game
    }));
  }

  public override render(): React.ReactNode {
    const {error, game, gameRemoved} = this.state;

    if(error) {
      return (
        <Typography sx={{color: 'error.main'}}>
          {error.message}
        </Typography>
      );
    } else if(!game) {
      if(gameRemoved) {
        return null;
      } else {
        return (
          <Typography>
            {`Loading Game ${this.props.gameId}`}
          </Typography>
        );
      }
    } else {
      return this.props.children(game);
    }
  }

}

export namespace GameProvider {
  export interface Props {
    gameId: GameId;
    children: (game: GameData) => React.ReactNode;
  }

  export interface State {
    game: GameData|null;
    error: Error|null;
    gameRemoved: boolean;
  }
}