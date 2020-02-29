// Generated using typescript-generator version 2.14.505 on 2020-02-29 13:40:14.

export interface CardDto extends RepresentationModel<CardDto> {
    id: string;
    projectId: string;
    columnId: string;
    name: string;
    description: string;
    priority: number;
}

export interface ColumnDto extends RepresentationModel<ColumnDto> {
    id: string;
    projectId: string;
    name: string;
    color: string;
    order: number;
}

export interface ProjectDto extends RepresentationModel<ProjectDto> {
    id: string;
    name: string;
    description: string;
}

export interface Links {
}

export interface RepresentationModel<T> {
    links: Links;
}
