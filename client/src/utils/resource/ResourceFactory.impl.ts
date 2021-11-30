import {autobind} from 'core-decorators';
import axios, {AxiosRequestConfig} from 'axios';
import {Service, Inject} from 'typedi';

import {User} from '../../user/interfaces';
import {Tokens} from '../../di';
import {Resource, ResourceFactory} from './interfaces';

@Service(ResourceFactory.Token)
@autobind
export class ResourceFactoryImpl implements ResourceFactory {
  @Inject(User.Token)
  private readonly user: User;
  @Inject(Tokens.API_HOST)
  private readonly apiHost: string;

  
  public build(apiPath: string): Resource {
    return new ResourceImpl(
      this.apiHost,
      apiPath,
      this.user.token
    );
  }
}

class ResourceImpl implements Resource {

  private readonly config: AxiosRequestConfig;

  constructor(
    private readonly apiHost: string,
    private readonly apiPath: string,
    userToken: string
  ) {
    this.config = {
      headers: {
        Authorization: `Bearer ${userToken}`
      }
    };
  }

  public async get<T>(path: string): Promise<T> {
    const {data} = await axios.get<T>(
      `http://${this.apiHost}${this.apiPath}${path}`, this.config);

    return data;
  }
  public async post<T>(path: string, body: object): Promise<T> {
    const {data} = await axios.post<T>(
      `http://${this.apiHost}${this.apiPath}${path}`,
      body as any,
      this.config
    );

    return data;
  }
  public async put<T>(path: string, body: object): Promise<T> {
    const {data} = await axios.put<T>(
      `http://${this.apiHost}${this.apiPath}${path}`,
      body as any,
      this.config
    );

    return data;
  }

  public async patch<T>(path: string, body: object): Promise<T> {
    const {data} = await axios.patch<T>(
      `${this.apiHost}${this.apiPath}${path}`,
      body as any,
      this.config
    );

    return data;
  }

  public async delete(path: string): Promise<void> {
    await axios.delete(
      `http://${this.apiHost}${this.apiPath}${path}`,
      this.config
    );
  }

}


