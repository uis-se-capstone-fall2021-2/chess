import {Alert, Button, ButtonGroup, Slider} from '@mui/material';
import {
  Celebration as CelebrationIcon,
  Download as DownloadIcon,
  FastForward as FastForwardIcon,
  FastRewind as FastRewindIcon,
  SkipNext as SkipNextIcon,
  SkipPrevious as SkipPreviousIcon
} from '@mui/icons-material';
import {autobind} from 'core-decorators';
// @ts-ignore
import {Chess} from 'chess.ts';
import {isNil} from 'lodash';
import * as React from 'react';
import {Chessboard} from 'react-chessboard';

import {MoveIntent} from '../../../board/interfaces';
import {BoardUtils} from '../../../board/BoardUtils';
import {Inject} from '../../../di';
import {GameService, GameData, GameStatus} from '../../interfaces';
import {RectContext} from '../../../utils/layout/RectContext';
import {User} from '../../../user/interfaces';
import {PlayerProvider} from '../../../player/components/PlayerProvider';

import './style.css';


@autobind
export class CompletedGame extends React.Component<CompletedGame.Props, CompletedGame.State> {

  @Inject(GameService.Token)
  private readonly gameService: GameService;
  @Inject(User.Token)
  private readonly user: User;

  private readonly game: Chess = new Chess();
  private readonly boards: string[] = [BoardUtils.STARTING_FEN];

  public override state: CompletedGame.State = {
    moveHistory: null,
    boardVersion: 0,
    sliderVersion: 0,
    error: null
  };

  public override async componentDidMount() {
    const {gameId} = this.props.game;
    try {
      const [moveHistory] = await Promise.all([
        this.gameService.getMoveHistory(gameId),
        this.gameService.fetchGameState(gameId).catch()
      ]);
      this.setState({
        moveHistory
      }, () => this.loadSnapshot(moveHistory.length));
    } catch(e) {
      this.setState({error: e as Error});
    }
  }

  private loadSnapshot(version: number): void {
    let snapshot: string;
    let i = version;
    do {
      snapshot = this.boards[i];
      if(snapshot) {
        break;
      }
      i--;
    } while(i >= 0);

    this.game.load(snapshot);

    if(i !== version) {
      const {moveHistory} = this.state;
      if(moveHistory) {
        const movesToReplay = moveHistory.slice(i, version);
        for(const move of movesToReplay) {
          this.game.move(move);
          this.boards[++i] = this.game.fen();
        }
      }
    }

    this.setState({
      boardVersion: version,
      sliderVersion: null
    });
  }
    
  public override render(): React.ReactNode {
    const {gameId, players} = this.props.game;
    const fen = this.game.fen();

    return (
      <RectContext.Consumer>
        {(dimensions: RectContext.Dimensions) => {
          const width = BoardUtils.getBoardWidth(dimensions);
          return (
            <>
              <this.Banner width={width}/>
              <Chessboard
                id={gameId}
                boardWidth={width}
                position={fen}
                boardOrientation={this.user.playerId === players[0] ? 'white' : 'black'}
                arePiecesDraggable={false}
              />
              <div style={{maxWidth: width, height: 12}}/>
              <div style={{maxWidth: width, display: 'flex', justifyContent: 'center'}}>
                <this.Controls/>
              </div>
            </>
          );
        }}
      </RectContext.Consumer>
    );
  }

  private Banner(props: {width: number}): React.ReactElement {
    const {game: gameState} = this.props;
    const {error, moveHistory, boardVersion} = this.state;
    const {width} = props;
    const sx = {maxWidth: width, marginBottom: 1};
    const whoseTurn = boardVersion % 2 === 0
      ? gameState.players[0]
      : gameState.players[1];
    const winner = gameState.winner;
    const loser = gameState.players.find(playerId => playerId !== winner);
    const userIsWinner = winner === this.user.playerId;

    switch(true) {
      case Boolean(error):
        return <Alert sx={sx} severity='error'>{error.message}</Alert>;
      case !moveHistory:
        return <Alert sx={sx} severity='warning'>Loading Move History</Alert>;
      case moveHistory?.length === boardVersion && gameState.status === GameStatus.TERMINATED:
        return (
          <Alert sx={sx} severity={userIsWinner ? 'success' : 'info'} icon={userIsWinner ? <CelebrationIcon/> : null}>
            <PlayerProvider playerId={loser}>
              {(player) => {
                const playerName = player?.displayName || `Player ${loser}`;
                return `${playerName} quit, `;
              }}
            </PlayerProvider>
            <PlayerProvider playerId={winner}>
              {(player) => {
                const playerName = player?.displayName || `Player ${winner}`;
                return `${playerName} wins!`;
              }}
            </PlayerProvider>
          </Alert>
        );
      case moveHistory?.length === boardVersion && gameState.status === GameStatus.COMPLETE:
        if(isNil(winner)) {
          return <Alert sx={sx} severity='warning'>Stalemate</Alert>;
        } else {
          return (
            <Alert sx={sx} severity={userIsWinner ? 'success' : 'info'} icon={userIsWinner ? <CelebrationIcon/> : null}>
              <PlayerProvider playerId={winner}>
                {(player) => {
                  const playerName = player?.displayName || `Player ${whoseTurn}`;
                  return `${playerName} wins!`;
                }}
              </PlayerProvider>
            </Alert>
          );
        }
      default:
        return (
          <Alert sx={sx} severity='info'>
            <PlayerProvider playerId={whoseTurn}>
              {(player) => {
                const playerName = player?.displayName || `Player ${whoseTurn}`;
                return `${playerName}'s turn`;
              }}
            </PlayerProvider>
          </Alert>
        );
    }
  }

  private Controls(): React.ReactElement {
    const {moveHistory, boardVersion, sliderVersion} = this.state;
    if(!moveHistory) {
      return null;
    }

    return (
      <div className='completed-game-controls'>
        <span style={{marginRight: 8}}>{`Move ${boardVersion}/${moveHistory.length}`}</span>
        <div>
          <ButtonGroup>
            <Button onClick={this.reset}>
              <FastRewindIcon/>
            </Button>
            <Button onClick={this.stepBack}>
              <SkipPreviousIcon/>
            </Button>
            <Button onClick={this.stepAhead}>
              <SkipNextIcon/>
            </Button>
            <Button onClick={this.skipToEnd}>
              <FastForwardIcon/>
            </Button>
            <Button onClick={this.downloadGame} color='primary'>
              <DownloadIcon/>
            </Button>
          </ButtonGroup>
        </div>
        <Slider
          sx={{
            maxWidth: '98%'
          }}
          defaultValue={0}
          step={1}
          marks
          min={0}
          max={moveHistory.length}
          value={isNil(sliderVersion) ? boardVersion : sliderVersion}
          onChange={this.onSliderChange}
          onChangeCommitted={this.onSliderChangeCommitted}
        />
      </div>
    );
  }

  private downloadGame(): void {
    this.gameService.download(this.props.game.gameId);
  }

  private onSliderChange(e: any, value: number|number[]) {
    this.setState({sliderVersion: Array.isArray(value) ? value[0] : value ?? 0});
  }

  private onSliderChangeCommitted(e: any, value: number|number[]) {
    this.loadSnapshot(Array.isArray(value) ? value[0] : value ?? 0);
  }

  private reset(): void {
    this.loadSnapshot(0);
  }

  private stepBack() {
    const target = Math.max(this.state.boardVersion - 1, 0);
    this.loadSnapshot(target);
  }

  private stepAhead(): void {
    const target = Math.min(this.state.boardVersion + 1, this.state.moveHistory.length);
    this.loadSnapshot(target);
  }

  private skipToEnd(): void {
    const target = (this.state.moveHistory.length);
    this.loadSnapshot(target);
  }

}

export namespace CompletedGame {
  export interface Props {
    game: GameData;
  }

  export interface State {
    boardVersion: number;
    sliderVersion: number;
    moveHistory: string[];
    error: Error;
  }
}