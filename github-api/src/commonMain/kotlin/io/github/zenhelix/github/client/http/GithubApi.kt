package io.github.zenhelix.github.client.http

import io.github.zenhelix.github.client.http.api.action.GithubActionsApi
import io.github.zenhelix.github.client.http.api.activity.GithubActivityApi
import io.github.zenhelix.github.client.http.api.apps.GithubAppsApi
import io.github.zenhelix.github.client.http.api.billing.GithubBillingApi
import io.github.zenhelix.github.client.http.api.branches.GithubBranchesApi
import io.github.zenhelix.github.client.http.api.checks.GithubChecksApi
import io.github.zenhelix.github.client.http.api.classroom.GithubClassroomApi
import io.github.zenhelix.github.client.http.api.codescanning.GithubCodeScanningApi
import io.github.zenhelix.github.client.http.api.codesecurity.GithubCodeSecurityApi
import io.github.zenhelix.github.client.http.api.codesofconduct.GithubCodesOfConductApi
import io.github.zenhelix.github.client.http.api.codespaces.GithubCodespacesApi
import io.github.zenhelix.github.client.http.api.collaborators.GithubCollaboratorsApi
import io.github.zenhelix.github.client.http.api.commits.GithubCommitsApi
import io.github.zenhelix.github.client.http.api.copilot.GithubCopilotApi
import io.github.zenhelix.github.client.http.api.dependabot.GithubDependabotApi
import io.github.zenhelix.github.client.http.api.dependencygraph.GithubDependencyGraphApi
import io.github.zenhelix.github.client.http.api.deploykeys.GithubDeployKeysApi
import io.github.zenhelix.github.client.http.api.deployment.GithubDeploymentsApi
import io.github.zenhelix.github.client.http.api.emoji.GithubEmojisApi
import io.github.zenhelix.github.client.http.api.gist.GithubGistsApi
import io.github.zenhelix.github.client.http.api.git.GithubGitApi
import io.github.zenhelix.github.client.http.api.gitignore.GithubGitignoreApi
import io.github.zenhelix.github.client.http.api.interactions.GithubInteractionsApi
import io.github.zenhelix.github.client.http.api.issues.GithubIssuesApi
import io.github.zenhelix.github.client.http.api.license.GithubLicensesApi
import io.github.zenhelix.github.client.http.api.markdown.GithubMarkdownApi
import io.github.zenhelix.github.client.http.api.meta.GithubMetaApi
import io.github.zenhelix.github.client.http.api.metric.GithubMetricsApi
import io.github.zenhelix.github.client.http.api.migration.GithubMigrationsApi
import io.github.zenhelix.github.client.http.api.organization.GithubOrganizationsApi
import io.github.zenhelix.github.client.http.api.packages.GithubPackagesApi
import io.github.zenhelix.github.client.http.api.pages.GithubPagesApi
import io.github.zenhelix.github.client.http.api.privateregistries.GithubPrivateRegistriesApi
import io.github.zenhelix.github.client.http.api.project.GithubProjectApi
import io.github.zenhelix.github.client.http.api.pullrequest.GithubPullRequestsApi
import io.github.zenhelix.github.client.http.api.ratelimit.GithubRateLimitApi
import io.github.zenhelix.github.client.http.api.reaction.GithubReactionsApi
import io.github.zenhelix.github.client.http.api.release.GithubReleasesApi
import io.github.zenhelix.github.client.http.api.repository.GithubRepositoriesApi
import io.github.zenhelix.github.client.http.api.search.GithubSearchApi
import io.github.zenhelix.github.client.http.api.secretscanning.GithubSecretScanningApi
import io.github.zenhelix.github.client.http.api.securityadvisory.GithubSecurityAdvisoriesApi
import io.github.zenhelix.github.client.http.api.team.GithubTeamsApi
import io.github.zenhelix.github.client.http.api.user.GithubUsersApi

public interface GithubApi : GithubActionsApi, GithubActivityApi, GithubAppsApi, GithubBillingApi, GithubBranchesApi, GithubChecksApi, GithubClassroomApi,
                             GithubCodeScanningApi, GithubCodeSecurityApi, GithubCodesOfConductApi,
                             GithubCodespacesApi, GithubCollaboratorsApi, GithubCommitsApi, GithubCopilotApi, GithubDependabotApi,
                             GithubDependencyGraphApi, GithubDeployKeysApi, GithubDeploymentsApi, GithubEmojisApi, GithubGistsApi,
                             GithubGitApi, GithubGitignoreApi, GithubInteractionsApi, GithubIssuesApi, GithubLicensesApi, GithubMarkdownApi,
                             GithubMetaApi, GithubMetricsApi, GithubMigrationsApi, GithubOrganizationsApi, GithubPackagesApi,
                             GithubPagesApi, GithubPrivateRegistriesApi, GithubProjectApi, GithubPullRequestsApi, GithubRateLimitApi, GithubReactionsApi,
                             GithubReleasesApi, GithubRepositoriesApi, GithubSearchApi, GithubSecretScanningApi, GithubSecurityAdvisoriesApi, GithubTeamsApi,
                             GithubUsersApi


