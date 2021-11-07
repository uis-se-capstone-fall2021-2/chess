import * as typedi from 'typedi';

export function Inject<V>(token: typedi.Token<V>): any {
  return function<T extends object, K extends keyof T>(target: T & {K: V}, key: K): any {
    Object.defineProperty(target, key, {
      get: function() {
        return typedi.Container.get(token);
      }
    });
    return target;
  };
}