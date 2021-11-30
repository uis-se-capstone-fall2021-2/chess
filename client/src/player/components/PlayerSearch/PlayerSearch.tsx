import {autobind} from 'core-decorators';
import {Debounce} from 'lodash-decorators';
import * as React from 'react';
import {Autocomplete, AutocompleteRenderInputParams, TextField} from '@mui/material';

import {Inject} from '../../../di';
import {Player, PlayerId, PlayerService, PlayerType} from '../../interfaces';

@autobind
export class PlayerSearch extends React.Component<PlayerSearch.Props, PlayerSearch.State> {
  @Inject(PlayerService.Token)
  private readonly playerService: PlayerService;

  public override readonly state: PlayerSearch.State = {
    query: null,
    results: []
  }

  public override render(): React.ReactNode {
    const {query, results} = this.state;
    const options = results.map((player: Player) => ({
      label: player.displayName,
      id: player.playerId
    }));
    return (
      <Autocomplete
        sx={{width: 350}}
        options={options}
        onChange={this.selectPlayer}
        renderInput={(params: AutocompleteRenderInputParams) => (
          <TextField
            label={this.props.label ?? 'Search Players'}
            {...params}
            value={query}
            onChange={this.onTextFieldChange}
           />
        )}
      />
    );
  }

  private selectPlayer(e: React.SyntheticEvent, value: string|{label: string, id: number}, reason: string): void {
    switch(reason) {
      case 'clear':
        this.setState({
          results: [],
          query: null
        });
        this.props.onSelect(null);
        return;
      case 'selectOption':
      case 'blur':
        const playerId = (value as {id: PlayerId})?.id;
        this.props.onSelect(
          this.playerService.getPlayer(playerId)
        );
        return;
    }
  }

  private onTextFieldChange(e: React.ChangeEvent<HTMLInputElement>) {
    const query = e.target.value;
    this.props.onSelect(null);
    this.updateQuery(query);
  }

  private updateQuery(query: string) {
    this.performQuery();
    this.setState({query});
  }

  @Debounce(100)
  private async performQuery(): Promise<void> {
    const results = await this.playerService.searchPlayers(this.state.query, this.props.playerType);
    this.setState({results});
  }
}

export namespace PlayerSearch {
  export interface Props {
    playerType: PlayerType;
    onSelect: (player: Player) => void;
    label?: React.ReactNode;
  }

  export interface State {
    query: string;
    results: Player[];
  }
}