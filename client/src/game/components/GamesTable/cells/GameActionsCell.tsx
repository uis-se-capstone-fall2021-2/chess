import {GridRenderCellParams} from '@mui/x-data-grid';
import {autobind} from 'core-decorators';
import * as React from 'react';
import {Route} from 'react-router';

import {Inject} from '../../../../di';
import {GameProvider} from '../../GameProvider';
import {GameData, GameId, GameService, GameState, GameStatus} from '../../../interfaces';
import {User} from '../../../../user/interfaces';
import {PendingGameActionsCell} from './PendingGameActionsCell';
import {ActiveGameActionsCell} from './ActiveGameActionsCell';
import {CompletedGameActionsCell} from './CompletedGameActionsCell';



@autobind
export class GameActionsCell extends React.Component<GridRenderCellParams<GameId>, {}> {
  @Inject(User.Token)
  private readonly user: User;
  @Inject(GameService.Token)
  private readonly gameService: GameService;

  public override render() {
    const {value: gameId} = this.props;
    return (
      <GameProvider gameId={gameId}>
        {(gameData: GameData) => {
          const {status} = gameData;
          switch(status) {
            case GameStatus.ACTIVE:
              return <ActiveGameActionsCell game={gameData as GameState} gameService={this.gameService}/>;
            case GameStatus.PENDING:
              return (
                <Route>
                  {({history}) => (
                    <PendingGameActionsCell
                      game={gameData}
                      gameService={this.gameService}
                      user={this.user}
                      onGameAccepted={(gameId: GameId) => history.push(`/games/${gameId}`)}/>
                  )}
                </Route>
              );
            
            case GameStatus.COMPLETE:
            case GameStatus.TERMINATED:
              return <CompletedGameActionsCell game={gameData} gameService={this.gameService}/>;
            case GameStatus.DECLINED:
            default:
              return <span/>
          }
        }}
      </GameProvider>
    );
  }
}





