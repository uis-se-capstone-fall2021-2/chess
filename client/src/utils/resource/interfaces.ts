import * as typedi from 'typedi';

export interface ResourceFactory {
  build(apiPath: string): Resource;
}

export namespace ResourceFactory {
  export const Token = new typedi.Token('ResourceFactory');
}

export interface Resource {
  get<T>(path: string): Promise<T>;
  post<T>(path: string, body: object): Promise<T>;
  put<T>(path: string, body: object): Promise<T>;
  delete(path: string): Promise<void>;
}

export function Resource(apiPath: string) {
  return function (target: any, propertyName: string, index?: number) {
    typedi.Container.registerHandler({
      object: target,
      propertyName,
      index,
      value: container => container.get<ResourceFactory>(ResourceFactory.Token).build(apiPath)
    });
  };
} 