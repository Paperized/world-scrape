export class Account {
  constructor(
    public username: string,
    public email: string,
    public authorities: string[],
    public creationTime: number,
    public isEnabled: boolean,
    public firstName: string,
    public lastName: string
  ) {
  }
}
