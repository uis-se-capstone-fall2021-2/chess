import {autobind} from 'core-decorators';
import {debounce} from 'lodash';
import * as React from 'react';
import ReactResizeObserver from 'react-resize-observer';


const rectContext = React.createContext<RectContext.Dimensions>({height: 0, width: 0});

export namespace RectContext {

  export type Dimensions = {
    width: number;
    height: number;
  }

  @autobind
  export class Observer extends React.Component<{}, Dimensions> {

    public override state: Dimensions = {
      height: 0,
      width: 0
    };

    public override render(): React.ReactNode {
      return (
        <>
          <ReactResizeObserver
            onResize={this.debouncedUpdateDimensions}
          />
          <rectContext.Provider value={this.state}>
            {this.props.children}
          </rectContext.Provider>
        </>
      );
    }

    private updateDimensions({height, width}: Dimensions): void {
      if(!(height === this.state?.height && width === this.state?.width)) {
        this.setState({height, width});
      }
    }

    private readonly debouncedUpdateDimensions = debounce(this.updateDimensions, 100);
  }

  export const Consumer = rectContext.Consumer;
}

