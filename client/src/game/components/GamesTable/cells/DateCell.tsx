import {GridRenderCellParams} from "@mui/x-data-grid";

export const DateCell = (params: GridRenderCellParams<Date>) => (<span>{params.value.toLocaleString()}</span>);