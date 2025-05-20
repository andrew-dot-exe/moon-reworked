
export class Success {
  successful: number;
  mood: number;
  contPeople: number;
  needContPeople: number;
  resources: number;
  central: number;
  search: number;
  
    constructor(successful: number, mood: number, contPeople: number, needContPeople: number, resources: number, central: number, search: number) {
      this.successful = successful
      this.mood = mood
      this.contPeople = contPeople
      this.needContPeople = needContPeople
      this.resources = resources
      this.central = central
      this.search = search
    }
}