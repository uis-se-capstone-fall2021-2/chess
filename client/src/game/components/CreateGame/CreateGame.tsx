import {autobind} from 'core-decorators';
import {Alert, Box, Select, MenuItem, SelectChangeEvent, Typography, Button} from '@mui/material';
import * as React from 'react';

import {Inject} from '../../../di';
import {PlayerSearch} from '../../../player/components/PlayerSearch';
import {User} from '../../../user/interfaces';
import {Player, PlayerColor, PlayerType} from '../../../player/interfaces';
import {GameService, GameState} from '../../interfaces';

@autobind
export class CreateGame extends React.Component<CreateGame.Props, CreateGame.State> {
  @Inject(GameService.Token)
  private readonly gameService: GameService;
  @Inject(User.Token)
  private readonly user: User;

  public override state: CreateGame.State = {
    opponentType: PlayerType.USER,
    opponent: null,
    playerColor: PlayerColor.WHITE,
    creatingGame: false,
    createGameError: null
  };

  public override render(): React.ReactNode {
    const {playerColor, opponentType, createGameError} = this.state;
    return (
      <Box>
        <Typography variant='h4'>New Game</Typography>
        <Box sx={{display: 'flex', flexDirection: 'row', flexBasis: 'min-content'}}>
          <Box sx={{
              display: 'flex',
              flexDirection: 'column',
              flexGrow: {
                xs: 1,
                sm: 0
              }
            }}>
            <Typography>Opponent</Typography>
            <Box sx={{
                display: 'flex',
                flexDirection: 'row',
                flexWrap: 'wrap',
                width: {
                  xs: '100%'
                }
              }}>
              <Select
                id='opponent-type-select'
                labelId='opponent-type-select__label'
                label='Type'
                value={opponentType}
                onChange={this.setOpponentType} >
                <MenuItem value={PlayerType.USER}>Human</MenuItem>
                <MenuItem value={PlayerType.AI}>Bot</MenuItem>
              </Select>
              <PlayerSearch
                label={opponentType === PlayerType.USER ? 'Search Players' : 'Choose Bot'}
                playerType={opponentType}
                onSelect={this.setOpponent}/>
            </Box>
            <Typography>Choose Your Color</Typography>
            <Box sx={{display: 'flex', flexDirection: 'row'}}>
              <Select
                id='self-color-select'
                labelId='self-color-select__label'
                label='Color'
                value={playerColor}
                onChange={this.setColor} >
                <MenuItem value={PlayerColor.WHITE}>{PlayerColor.WHITE.toString()}</MenuItem>
                <MenuItem value={PlayerColor.BLACK}>{PlayerColor.BLACK.toString()}</MenuItem>
              </Select>
            </Box>
            {createGameError ? <Alert severity='error'>{createGameError.message}</Alert> : null}
            <Box sx={{display: 'flex', flexDirection: 'row', justifyContent: 'flex-end'}}>
              <Button onClick={this.createGame} disabled={!this.canCreateGame}>
                {opponentType === PlayerType.USER ? 'Send Invite' : 'Start Game'}
              </Button>
            </Box>
          </Box>
        </Box>
      </Box>
    );
  }

  private setOpponentType(e: SelectChangeEvent<PlayerType>): void {
    const opponentType = e.target.value as PlayerType;
    this.setState({
      opponentType
    });
  }

  private setOpponent(player: Player): void {
    this.setState({
      opponent: player,
      createGameError: (player?.playerId === this.user.playerId)
        ? new Error('Opponent cannot be self')
        : null
    });
  }

  private setColor(e: SelectChangeEvent<PlayerColor>): void {
    const playerColor = e.target.value as PlayerColor;
    this.setState({
      playerColor,
      createGameError: null
    });
  }

  private get canCreateGame(): boolean {
    const {opponent, playerColor, creatingGame, createGameError} = this.state;
    if(creatingGame || createGameError) {
      return false;
    }
    return Boolean(opponent && playerColor);
  }

  private async createGame(): Promise<void> {
    const {opponent, playerColor} = this.state;
    this.setState({creatingGame: true});
    try {
      const game: GameState = await this.gameService.createGame({
        opponent,
        playerColor
      });
      this.props.onNewGame(game);
    } catch(e) {
      this.setState({createGameError: e as Error});
    } finally {
      this.setState({creatingGame: false});
    }
  }
}

export namespace CreateGame {
  export interface Props {
    onNewGame(game: GameState): void;
  }

  export interface State {
    opponentType: PlayerType;
    opponent: Player;
    playerColor: PlayerColor;
    creatingGame: boolean;
    createGameError: Error;
  }
}