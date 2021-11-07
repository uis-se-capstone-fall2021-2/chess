import {autobind} from 'core-decorators';

import {GameData} from '../../interfaces';
import {GamesTable} from '../GamesTable/GamesTable';


@autobind
export class GameHistoryTable extends GamesTable {
  protected override async loadGames(): Promise<GameData[]> {
    return this.playerService.getOwnGameHistory();
  }
}