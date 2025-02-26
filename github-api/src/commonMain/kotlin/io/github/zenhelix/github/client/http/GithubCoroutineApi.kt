package io.github.zenhelix.github.client.http

import io.github.zenhelix.github.client.http.api.action.GithubActionsCoroutineApi
import io.github.zenhelix.github.client.http.api.activity.GithubActivityCoroutineApi
import io.github.zenhelix.github.client.http.api.apps.GithubAppsCoroutineApi
import io.github.zenhelix.github.client.http.api.billing.GithubBillingCoroutineApi
import io.github.zenhelix.github.client.http.api.branches.GithubBranchesCoroutineApi
import io.github.zenhelix.github.client.http.api.checks.GithubChecksCoroutineApi
import io.github.zenhelix.github.client.http.api.classroom.GithubClassroomCoroutineApi
import io.github.zenhelix.github.client.http.api.codescanning.GithubCodeScanningCoroutineApi
import io.github.zenhelix.github.client.http.api.codesecurity.GithubCodeSecurityCoroutineApi
import io.github.zenhelix.github.client.http.api.codesofconduct.GithubCodesOfConductCoroutineApi
import io.github.zenhelix.github.client.http.api.codespaces.GithubCodespacesCoroutineApi
import io.github.zenhelix.github.client.http.api.collaborators.GithubCollaboratorsCoroutineApi
import io.github.zenhelix.github.client.http.api.commits.GithubCommitsCoroutineApi
import io.github.zenhelix.github.client.http.api.copilot.GithubCopilotCoroutineApi
import io.github.zenhelix.github.client.http.api.dependabot.GithubDependabotCoroutineApi
import io.github.zenhelix.github.client.http.api.dependencygraph.GithubDependencyGraphCoroutineApi
import io.github.zenhelix.github.client.http.api.deploykeys.GithubDeployKeysCoroutineApi
import io.github.zenhelix.github.client.http.api.deployment.GithubDeploymentsCoroutineApi
import io.github.zenhelix.github.client.http.api.emoji.GithubEmojisCoroutineApi
import io.github.zenhelix.github.client.http.api.gist.GithubGistsCoroutineApi
import io.github.zenhelix.github.client.http.api.git.GithubGitCoroutineApi
import io.github.zenhelix.github.client.http.api.gitignore.GithubGitignoreCoroutineApi
import io.github.zenhelix.github.client.http.api.interactions.GithubInteractionsCoroutineApi
import io.github.zenhelix.github.client.http.api.issues.GithubIssuesCoroutineApi
import io.github.zenhelix.github.client.http.api.license.GithubLicensesCoroutineApi
import io.github.zenhelix.github.client.http.api.markdown.GithubMarkdownCoroutineApi
import io.github.zenhelix.github.client.http.api.meta.GithubMetaCoroutineApi
import io.github.zenhelix.github.client.http.api.metric.GithubMetricsCoroutineApi
import io.github.zenhelix.github.client.http.api.migration.GithubMigrationsCoroutineApi
import io.github.zenhelix.github.client.http.api.organization.GithubOrganizationsCoroutineApi
import io.github.zenhelix.github.client.http.api.packages.GithubPackagesCoroutineApi
import io.github.zenhelix.github.client.http.api.privateregistries.GithubPrivateRegistriesCoroutineApi
import io.github.zenhelix.github.client.http.api.project.GithubProjectCoroutineApi
import io.github.zenhelix.github.client.http.api.pullrequest.GithubPullRequestsCoroutineApi
import io.github.zenhelix.github.client.http.api.ratelimit.GithubRateLimitCoroutineApi
import io.github.zenhelix.github.client.http.api.reaction.GithubReactionsCoroutineApi
import io.github.zenhelix.github.client.http.api.release.GithubReleasesCoroutineApi
import io.github.zenhelix.github.client.http.api.repository.GithubRepositoriesCoroutineApi
import io.github.zenhelix.github.client.http.api.search.GithubSearchCoroutineApi
import io.github.zenhelix.github.client.http.api.secretscanning.GithubSecretScanningCoroutineApi
import io.github.zenhelix.github.client.http.api.securityadvisory.GithubSecurityAdvisoriesCoroutineApi
import io.github.zenhelix.github.client.http.api.team.GithubTeamsCoroutineApi
import io.github.zenhelix.github.client.http.api.user.GithubUsersCoroutineApi

public interface GithubCoroutineApi : GithubActionsCoroutineApi, GithubActivityCoroutineApi, GithubAppsCoroutineApi, GithubBillingCoroutineApi,
                                      GithubBranchesCoroutineApi, GithubChecksCoroutineApi, GithubClassroomCoroutineApi, GithubCodeScanningCoroutineApi,
                                      GithubCodeSecurityCoroutineApi, GithubCodesOfConductCoroutineApi, GithubCodespacesCoroutineApi,
                                      GithubCollaboratorsCoroutineApi, GithubCommitsCoroutineApi, GithubCopilotCoroutineApi, GithubDependabotCoroutineApi,
                                      GithubDependencyGraphCoroutineApi, GithubDeployKeysCoroutineApi, GithubDeploymentsCoroutineApi, GithubEmojisCoroutineApi,
                                      GithubGistsCoroutineApi, GithubGitCoroutineApi, GithubGitignoreCoroutineApi, GithubInteractionsCoroutineApi,
                                      GithubIssuesCoroutineApi, GithubLicensesCoroutineApi, GithubMarkdownCoroutineApi, GithubMetaCoroutineApi,
                                      GithubMetricsCoroutineApi, GithubMigrationsCoroutineApi, GithubOrganizationsCoroutineApi, GithubPackagesCoroutineApi,
                                      GithubPrivateRegistriesCoroutineApi, GithubProjectCoroutineApi, GithubPullRequestsCoroutineApi,
                                      GithubRateLimitCoroutineApi, GithubReactionsCoroutineApi, GithubReleasesCoroutineApi, GithubRepositoriesCoroutineApi,
                                      GithubSearchCoroutineApi, GithubSecretScanningCoroutineApi, GithubSecurityAdvisoriesCoroutineApi, GithubTeamsCoroutineApi,
                                      GithubUsersCoroutineApi
